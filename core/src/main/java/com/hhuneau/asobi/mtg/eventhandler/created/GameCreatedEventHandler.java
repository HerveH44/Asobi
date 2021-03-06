package com.hhuneau.asobi.mtg.eventhandler.created;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.eventhandler.GameEventHandler;
import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameService;
import com.hhuneau.asobi.mtg.player.Player;
import com.hhuneau.asobi.mtg.pool.PoolService;
import com.hhuneau.asobi.websocket.events.game.host.KickPlayerEvent;
import com.hhuneau.asobi.websocket.events.game.host.StartGameEvent;
import com.hhuneau.asobi.websocket.events.game.host.SwapPlayerEvent;
import com.hhuneau.asobi.websocket.events.game.player.JoinGameEvent;
import com.hhuneau.asobi.websocket.events.game.player.LeaveGameEvent;
import com.hhuneau.asobi.websocket.messages.ErrorMessage;
import com.hhuneau.asobi.websocket.messages.PlayerIdMessage;

import java.util.function.Consumer;

import static com.hhuneau.asobi.mtg.game.Status.CREATED;

public abstract class GameCreatedEventHandler extends GameEventHandler {

    final CustomerService customerService;
    final PoolService poolService;

    protected GameCreatedEventHandler(GameService gameService, CustomerService customerService, PoolService poolService) {
        super(gameService);
        this.customerService = customerService;
        this.poolService = poolService;
    }


    @Override
    public void handle(Game game, JoinGameEvent evt) {
        final String sessionId = evt.sessionId;
        final String name = evt.name;
        game.getPlayers().stream()
            .filter(player -> player.getPlayerId() == evt.playerId)
            .findFirst()
            .ifPresentOrElse(
                changeUserId(game, sessionId),
                addPlayer(game, sessionId, name)
            );
    }

    private Runnable addPlayer(Game game, String sessionId, String name) {
        return () -> {
            final Player player = Player.of(sessionId, name, false);
            if (!game.isFull()) {
                final Player savedPlayer = gameService.addPlayer(game, player);
                sendPlayerIdMessage(sessionId, savedPlayer);
            } else {
                customerService.send(sessionId, ErrorMessage.of("game " + game.getGameId() + " is already full"));
            }
        };
    }

    private Consumer<Player> changeUserId(Game game, String sessionId) {
        return player -> {
            // if the player was the host
            if (player.getUserId().equals(game.getHostId())) {
                game.setHostId(sessionId);
            }
            player.setUserId(sessionId);
            gameService.save(game);
            sendPlayerIdMessage(sessionId, player);
        };
    }

    private void sendPlayerIdMessage(String sessionId, Player player) {
        final PlayerIdMessage message = PlayerIdMessage.of(player.getPlayerId());
        customerService.send(sessionId, message);
    }

    @Override
    public void handle(Game game, LeaveGameEvent evt) {
        final boolean isHost = game.getHostId().equals(evt.sessionId);
        if (!isHost && game.getPlayers().removeIf(player -> player.getPlayerId() == evt.playerId)) {
            gameService.save(game);
        }
    }

    @Override
    public void handle(Game game, StartGameEvent evt) {
        gameService.startGame(evt);
        poolService.createPools(game);
    }

    @Override
    public void handle(Game game, KickPlayerEvent evt) {
        if (game.getAuthToken().equals(evt.authToken)) {
            gameService.kick(game, evt.kick);
        } else {
            customerService.send(evt.sessionId, ErrorMessage.of("operation not permitted"));
        }
    }

    @Override
    public void handle(Game game, SwapPlayerEvent evt) {
        if (game.getAuthToken().equals(evt.authToken)) {
            gameService.swap(game, evt.swap);
        } else {
            customerService.send(evt.sessionId, ErrorMessage.of("operation not permitted"));
        }
    }

    @Override
    public boolean isInterested(Game game) {
        return game.getStatus().equals(CREATED);
    }
}

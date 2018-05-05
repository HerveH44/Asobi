package com.hhuneau.asobi.game.eventhandler;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.game.Game;
import com.hhuneau.asobi.game.GameService;
import com.hhuneau.asobi.game.player.Player;
import com.hhuneau.asobi.websocket.events.game.StartGameEvent;
import com.hhuneau.asobi.websocket.events.game.player.JoinGameEvent;
import com.hhuneau.asobi.websocket.events.game.player.LeaveGameEvent;
import com.hhuneau.asobi.websocket.messages.ErrorMessage;
import com.hhuneau.asobi.websocket.messages.PlayerIdMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

import static com.hhuneau.asobi.game.Status.CREATED;

@Component
@Transactional
public class GameCreatedEventHandler implements EventHandler {
    private final GameService gameService;
    private final CustomerService customerService;

    public GameCreatedEventHandler(GameService gameService, CustomerService customerService) {
        this.gameService = gameService;
        this.customerService = customerService;
    }

    @Override
    public void handle(Game game, JoinGameEvent evt) {
        final String sessionId = evt.sessionId;
        final String name = evt.name;
        game.getPlayers().stream()
            .filter(player -> player.getUserId().equals(sessionId))
            .findFirst()
            .ifPresentOrElse(
                changeUserId(game, sessionId),
                addPlayer(game, sessionId, name)
            );
    }

    private Runnable addPlayer(Game game, String sessionId, String name) {
        return () -> {
            final Player player = Player.of(sessionId, name);
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

    }

    @Override
    public void handle(Game game, StartGameEvent evt) {

    }

    @Override
    public boolean isInterested(Game game) {
        return game.getStatus().equals(CREATED);
    }
}

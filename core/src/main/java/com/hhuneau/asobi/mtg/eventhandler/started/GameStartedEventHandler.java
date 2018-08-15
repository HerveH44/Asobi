package com.hhuneau.asobi.mtg.eventhandler.started;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.eventhandler.EventHandler;
import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameService;
import com.hhuneau.asobi.mtg.player.PlayerService;
import com.hhuneau.asobi.websocket.events.game.player.*;
import com.hhuneau.asobi.websocket.messages.PackMessage;
import com.hhuneau.asobi.websocket.messages.ReconnectMessage;
import org.springframework.transaction.annotation.Transactional;

import static com.hhuneau.asobi.mtg.game.Status.STARTED;

public abstract class GameStartedEventHandler implements EventHandler {

    final CustomerService customerService;
    final PlayerService playerService;
    final GameService gameService;

    protected GameStartedEventHandler(CustomerService customerService, PlayerService playerService, GameService gameService) {
        this.customerService = customerService;
        this.playerService = playerService;
        this.gameService = gameService;
    }

    @Override
    public void handle(Game game, JoinGameEvent evt) {
        game.getPlayers().stream()
            .filter(player -> player.getPlayerId() == evt.playerId)
            .findFirst()
            .ifPresent(player -> {
                player.setUserId(evt.sessionId);
                customerService.send(evt.sessionId, ReconnectMessage.of(player.getPlayerState().getPickedCards()));

                if(player.getPlayerState().hasWaitingPack()) {
                    customerService.send(evt.sessionId, PackMessage.of(player.getPlayerState().getWaitingPack()));
                }
            });
    }

    @Override
    public void handle(Game game, LeaveGameEvent evt) {
        game.getPlayers().stream()
            .filter(player -> player.getPlayerId() == evt.playerId)
            .findFirst()
            .ifPresent(player -> player.setUserId(""));
    }

    @Override
    public void handle(Game game, HashDeckEvent evt) {
        game.getPlayers().stream()
            .filter(player -> player.getPlayerId() == evt.playerId)
            .findFirst()
            .ifPresent(player -> playerService.hashDeck(player, evt.main, evt.side));
    }

    @Override
    public void handle(Game game, PlayerNameEvent evt) {
        gameService.setPlayerName(game, evt);
    }

    @Override
    public void handle(Game game, MessageEvent evt) {
        gameService.addMessage(game, evt);
    }


    @Override
    public boolean isInterested(Game game) {
        return game.getStatus().equals(STARTED);
    }
}

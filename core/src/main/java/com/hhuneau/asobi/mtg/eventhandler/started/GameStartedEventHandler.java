package com.hhuneau.asobi.mtg.eventhandler.started;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.eventhandler.GameEventHandler;
import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameService;
import com.hhuneau.asobi.mtg.player.PlayerService;
import com.hhuneau.asobi.websocket.events.game.player.HashDeckEvent;
import com.hhuneau.asobi.websocket.events.game.player.JoinGameEvent;
import com.hhuneau.asobi.websocket.events.game.player.LeaveGameEvent;
import com.hhuneau.asobi.websocket.messages.PackMessage;
import com.hhuneau.asobi.websocket.messages.ReconnectMessage;

public abstract class GameStartedEventHandler extends GameEventHandler {

    final CustomerService customerService;
    final PlayerService playerService;

    protected GameStartedEventHandler(GameService gameService, CustomerService customerService, PlayerService playerService) {
        super(gameService);
        this.customerService = customerService;
        this.playerService = playerService;
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
        gameService.save(game);
    }

    @Override
    public void handle(Game game, HashDeckEvent evt) {
        game.getPlayers().stream()
            .filter(player -> player.getPlayerId() == evt.playerId)
            .findFirst()
            .ifPresent(player -> playerService.hashDeck(player, evt.main, evt.side));
    }

    @Override
    public boolean isInterested(Game game) {
        return game.getStatus().hasStarted();
    }
}

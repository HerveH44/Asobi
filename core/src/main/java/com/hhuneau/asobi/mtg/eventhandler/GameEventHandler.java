package com.hhuneau.asobi.mtg.eventhandler;

import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameService;
import com.hhuneau.asobi.websocket.events.game.player.MessageEvent;
import com.hhuneau.asobi.websocket.events.game.player.PlayerNameEvent;

// Here are regrouped all the methods that
// are not dependent from gameType or the game status
public abstract class GameEventHandler implements EventHandler {

    protected final GameService gameService;

    protected GameEventHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void handle(Game game, PlayerNameEvent evt) {
        gameService.setPlayerName(game, evt);
    }

    @Override
    public void handle(Game game, MessageEvent evt) {
        gameService.addMessage(game, evt);
    }
}

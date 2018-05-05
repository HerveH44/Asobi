package com.hhuneau.asobi.websocket.events.game;

import com.hhuneau.asobi.game.Game;
import com.hhuneau.asobi.game.eventhandler.EventHandler;

public class StartGameEvent extends GameEvent {
    public String authToken;

    @Override
    public void accepts(Game game, EventHandler eventHandler) {
        eventHandler.handle(game, this);
    }
}

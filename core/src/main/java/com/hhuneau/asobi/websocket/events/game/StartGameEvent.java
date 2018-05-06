package com.hhuneau.asobi.websocket.events.game;

import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.eventhandler.EventHandler;

public class StartGameEvent extends GameEvent {
    public String authToken;

    @Override
    public void accepts(Game game, EventHandler eventHandler) {
        eventHandler.handle(game, this);
    }
}

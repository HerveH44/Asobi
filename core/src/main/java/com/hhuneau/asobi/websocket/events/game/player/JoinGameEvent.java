package com.hhuneau.asobi.websocket.events.game.player;

import com.hhuneau.asobi.game.Game;
import com.hhuneau.asobi.game.eventhandler.EventHandler;

public class JoinGameEvent extends PlayerEvent {
    public String name;

    @Override
    public void accepts(Game game, EventHandler eventHandler) {
        eventHandler.handle(game, this);
    }
}

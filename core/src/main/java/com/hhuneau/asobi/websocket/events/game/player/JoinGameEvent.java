package com.hhuneau.asobi.websocket.events.game.player;

import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.eventhandler.EventHandler;

public class JoinGameEvent extends PlayerEvent {
    public String name;

    @Override
    public void accepts(Game game, EventHandler eventHandler) {
        eventHandler.handle(game, this);
    }
}

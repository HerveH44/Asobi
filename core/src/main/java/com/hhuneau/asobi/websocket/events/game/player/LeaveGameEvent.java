package com.hhuneau.asobi.websocket.events.game.player;

import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.eventhandler.EventHandler;

public class LeaveGameEvent extends PlayerEvent {
    @Override
    public void accepts(Game game, EventHandler eventHandler) {
        eventHandler.handle(game, this);
    }
}

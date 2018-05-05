package com.hhuneau.asobi.websocket.events.game.player;

import com.hhuneau.asobi.game.Game;
import com.hhuneau.asobi.game.eventhandler.EventHandler;

public class LeaveGameEvent extends PlayerEvent {
    @Override
    public void accepts(Game game, EventHandler eventHandler) {
        eventHandler.handle(game, this);
    }
}

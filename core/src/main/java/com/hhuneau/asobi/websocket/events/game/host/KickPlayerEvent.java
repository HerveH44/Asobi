package com.hhuneau.asobi.websocket.events.game.host;

import com.hhuneau.asobi.mtg.eventhandler.EventHandler;
import com.hhuneau.asobi.mtg.game.Game;

public class KickPlayerEvent extends HostEvent {

    public long kick;

    @Override
    public void accepts(Game game, EventHandler eventHandler) {
        eventHandler.handle(game, this);
    }
}

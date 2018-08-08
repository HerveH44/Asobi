package com.hhuneau.asobi.websocket.events.game.host;

import com.hhuneau.asobi.mtg.eventhandler.EventHandler;
import com.hhuneau.asobi.mtg.game.Game;

import java.util.List;

public class SwapPlayerEvent extends HostEvent {

    public List<Long> swap;

    @Override
    public void accepts(Game game, EventHandler eventHandler) {
        eventHandler.handle(game, this);
    }
}

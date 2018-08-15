package com.hhuneau.asobi.websocket.events.game.player;

import com.hhuneau.asobi.mtg.eventhandler.EventHandler;
import com.hhuneau.asobi.mtg.game.Game;

public class MessageEvent extends PlayerEvent {

    public String message;

    @Override
    public void accepts(Game game, EventHandler eventHandler) {
        eventHandler.handle(game, this);
    }
}

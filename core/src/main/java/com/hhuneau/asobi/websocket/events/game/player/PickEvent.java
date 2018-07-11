package com.hhuneau.asobi.websocket.events.game.player;

import com.hhuneau.asobi.mtg.eventhandler.EventHandler;
import com.hhuneau.asobi.mtg.game.Game;

public class PickEvent extends PlayerEvent {
    public String cardIndex;

    @Override
    public void accepts(Game game, EventHandler eventHandler) {
        eventHandler.handle(game, this);
    }
}

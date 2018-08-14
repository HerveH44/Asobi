package com.hhuneau.asobi.websocket.events.game.player;

import com.hhuneau.asobi.mtg.eventhandler.EventHandler;
import com.hhuneau.asobi.mtg.game.Game;

import java.util.List;

public class HashDeckEvent extends PlayerEvent {

    public List<String> main;
    public List<String> side;

    @Override
    public void accepts(Game game, EventHandler eventHandler) {
        eventHandler.handle(game, this);
    }
}

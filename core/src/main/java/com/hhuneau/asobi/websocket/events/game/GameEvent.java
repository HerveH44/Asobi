package com.hhuneau.asobi.websocket.events.game;

import com.hhuneau.asobi.game.Game;
import com.hhuneau.asobi.game.eventhandler.EventHandler;
import com.hhuneau.asobi.websocket.events.Event;

public abstract class GameEvent extends Event {
    public long gameId;

    abstract public void accepts(Game game, EventHandler eventHandler);
}

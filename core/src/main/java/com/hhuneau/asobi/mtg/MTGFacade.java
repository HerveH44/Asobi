package com.hhuneau.asobi.mtg;

import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.events.SessionConnectedEvent;
import com.hhuneau.asobi.websocket.events.game.GameEvent;

public interface MTGFacade {

    void handle(SessionConnectedEvent event);

    void handle(CreateGameEvent event);

    void handle(GameEvent event);

    void broadcastState(long gameId);
}

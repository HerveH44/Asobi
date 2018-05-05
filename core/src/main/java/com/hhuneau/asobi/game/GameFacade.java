package com.hhuneau.asobi.game;

import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.events.game.GameEvent;

public interface GameFacade {

    void handle(CreateGameEvent event);
    void handle(GameEvent event);
}

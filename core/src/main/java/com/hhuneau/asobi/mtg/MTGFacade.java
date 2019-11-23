package com.hhuneau.asobi.mtg;

import com.hhuneau.asobi.mtg.game.AuthTokenDTO;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.events.game.GameEvent;

public interface MTGFacade {

    void disconnectUser(String sessionId);

    AuthTokenDTO handle(CreateGameEvent event);

    void handle(GameEvent event);

    void broadcastState(long gameId);
}

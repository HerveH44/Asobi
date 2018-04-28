package com.hhuneau.asobi.game;

import com.hhuneau.asobi.websocket.events.CreateGameEvent;

import java.util.Optional;

public interface GameService {

    Optional<Game> getGame(long gameId);

    void startGame(Game game);

    long createGame(CreateGameEvent evt);
}

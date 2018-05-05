package com.hhuneau.asobi.game;

import com.hhuneau.asobi.game.player.Player;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;

import java.util.Optional;

public interface GameService {

    Optional<Game> getGame(long gameId);

    CreateGameDTO createGame(CreateGameEvent evt);

    boolean isPresent(long gameId);

    boolean hasStarted(long gameId);

    Player addPlayer(Game game, Player player);

    void startGame(long gameId);

    void finishGame(long gameId);

    boolean canStart(Game game, String authToken);

    void broadcastState(Game game);

    void save(Game game);
}

package com.hhuneau.asobi.game;

import com.hhuneau.asobi.game.actions.creategame.CreateGameDTO;
import com.hhuneau.asobi.game.player.Player;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;

import java.util.Optional;

public interface GameService {

    Optional<Game> getGame(long gameId);

    CreateGameDTO createGame(CreateGameEvent evt);

    void startGame(long gameId, String authToken);

    boolean isPresent(long gameId);

    boolean hasStarted(long gameId);

    boolean accept(long gameId, Player player);
}

package com.hhuneau.asobi.game;

import java.util.Optional;

public interface GameService {
    Optional<Game> getGame(long gameId);
}

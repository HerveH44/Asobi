package com.hhuneau.asobi.game.player;

import java.util.Optional;

public interface PlayerService {

    Player save(Player player);

    Optional<Player> getPlayerWithId(long gameId, long playerId);
}

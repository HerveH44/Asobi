package com.hhuneau.asobi.game.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query(value = "select * from Player where player_id = ?1 AND game_id = ?2", nativeQuery = true)
    Optional<Player> findByPlayerIdAndGameId(long playerId, long gameId);

    @Query(value = "select * from Player where player_id = ?1 AND game_id = ?2 AND user_id = ?3", nativeQuery = true)
    Optional<Player> findByPlayerIdAndGameIdAndUserId(long playerId, long gameId, String userId);

    @Query(value = "select * from Player where game_id = ?1 AND user_id = ?2", nativeQuery = true)
    Optional<Player> findByGameIdAndUserId(long playerId, String sessionId);
}

package com.hhuneau.asobi.game.player;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class DefaultPlayerService implements PlayerService {

    private final PlayerRepository playerRepository;

    public DefaultPlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Player save(Player player) {
        return playerRepository.save(player);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Player> getPlayerWithId(long gameId, long playerId) {
        return playerRepository.findByPlayerIdAndGameId(playerId, gameId);
    }

    @Override
    @Transactional
    public void remove(long gameId, long playerId, String sessionId) {
        playerRepository.findByPlayerIdAndGameIdAndUserId(playerId, gameId, sessionId)
            .ifPresent(playerRepository::delete);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Player> getPlayerWithUserId(long gameId, String sessionId) {
        return playerRepository.findByGameIdAndUserId(gameId, sessionId);
    }
}

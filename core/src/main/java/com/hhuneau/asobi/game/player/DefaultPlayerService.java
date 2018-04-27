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
        return playerRepository.findAll().stream()
            .filter(player -> (player.getPlayerId() == playerId) && (player.getGame().getGameId() == gameId))
            .findFirst();
    }
}

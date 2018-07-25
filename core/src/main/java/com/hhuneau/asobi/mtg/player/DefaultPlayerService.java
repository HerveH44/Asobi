package com.hhuneau.asobi.mtg.player;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DefaultPlayerService implements PlayerService {

    private final PlayerRepository playerRepository;

    public DefaultPlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public void save(Player player) {
        playerRepository.save(player);
    }

    @Override
    public void disconnectPlayersWithSession(String sessionId) {
        final List<Player> playerList = playerRepository.findAllByUserId(sessionId).stream()
            .peek(player -> player.setUserId(""))
            .collect(Collectors.toList());
        playerRepository.saveAll(playerList);
    }
}

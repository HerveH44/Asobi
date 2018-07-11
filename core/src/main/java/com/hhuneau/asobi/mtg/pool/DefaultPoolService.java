package com.hhuneau.asobi.mtg.pool;

import com.hhuneau.asobi.mtg.game.Game;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DefaultPoolService implements PoolService {

    private final PoolRepository poolRepository;
    private final List<PoolMaker> poolMakers;

    public DefaultPoolService(PoolRepository poolRepository, List<PoolMaker> poolMakers) {
        this.poolRepository = poolRepository;
        this.poolMakers = poolMakers;
    }

    @Override
    @Transactional
    public void createPools(Game game) {
        final List<Booster> boosters = poolMakers.stream()
            .filter(poolMaker -> poolMaker.isInterested(game.getGameMode()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException(String.format("gameMode %s is not supported", game.getGameMode())))
            .createPools(game);
        poolRepository.saveAll(boosters);
    }

    @Override
    public void delete(Booster booster) {
        poolRepository.delete(booster);
    }
}

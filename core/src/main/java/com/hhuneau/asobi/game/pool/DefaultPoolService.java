package com.hhuneau.asobi.game.pool;

import com.hhuneau.asobi.game.Game;
import com.hhuneau.asobi.game.GameMode;
import com.hhuneau.asobi.game.player.Player;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class DefaultPoolService implements PoolService {

    private final PoolRepository repository;
    private final PoolMaker poolMaker;

    public DefaultPoolService(PoolRepository repository, PoolMaker poolMaker) {
        this.repository = repository;
        this.poolMaker = poolMaker;
    }

    @Override
    @Transactional
    public void createPools(Set<Player> players, Game game) {
        if (game.getGameMode().equals(GameMode.NORMAL)) {
            final List<Booster> boosters = poolMaker.createPools(players, game.getSets());
            repository.saveAll(boosters);
        }
    }

}

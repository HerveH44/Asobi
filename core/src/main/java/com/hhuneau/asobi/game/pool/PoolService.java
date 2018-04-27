package com.hhuneau.asobi.game.pool;

import com.hhuneau.asobi.game.Game;
import com.hhuneau.asobi.game.GameMode;
import com.hhuneau.asobi.game.player.Player;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class PoolService {

    private final PoolRepository repository;
    private final PoolMaker poolMaker;

    public PoolService(PoolRepository repository, PoolMaker poolMaker) {
        this.repository = repository;
        this.poolMaker = poolMaker;
    }

    @Transactional
    public void createPools(Set<Player> players, Game game) {
        if (game.getGameMode().equals(GameMode.NORMAL)) {
            final List<Booster> boosters = poolMaker.createPools(players, game.getSets());
            repository.save(boosters);
        }
    }

}

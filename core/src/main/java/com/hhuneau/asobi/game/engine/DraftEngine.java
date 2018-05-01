package com.hhuneau.asobi.game.engine;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.game.Game;
import com.hhuneau.asobi.game.GameType;
import com.hhuneau.asobi.game.pool.Booster;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hhuneau.asobi.game.GameType.DRAFT;

@Component
public class DraftEngine implements GameEngine {

    private final CustomerService customerService;

    public DraftEngine(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    @Transactional
    public void start(Game game) {
        game.getPlayers().forEach(player -> {
            final List<Booster> pool = player.getPool();
            if (!pool.isEmpty()) {
                //put the first booster as available

//                customerService.send(player.getUserId(), PlayerStateMessage.of());
            }
        });
    }

    @Override
    public boolean isInterested(GameType gameType) {
        return gameType.equals(DRAFT);
    }
}

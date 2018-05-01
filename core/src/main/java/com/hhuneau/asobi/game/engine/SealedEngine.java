package com.hhuneau.asobi.game.engine;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.game.Game;
import com.hhuneau.asobi.game.GameType;
import com.hhuneau.asobi.game.pool.Booster;
import com.hhuneau.asobi.websocket.messages.PoolMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hhuneau.asobi.game.GameType.SEALED;

@Component
public class SealedEngine implements GameEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(SealedEngine.class);
    private final CustomerService customerService;

    public SealedEngine(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    @Transactional
    //TODO: Refactor as part of Facade or action
    public void start(Game game) {
        //TODO: Responsability of playerService?
        game.getPlayers().forEach(player -> {
            final List<Booster> pool = player.getPool();
            customerService.send(player.getUserId(), PoolMessage.of(pool));
        });
    }

    @Override
    public boolean isInterested(GameType gameType) {
        return gameType.equals(SEALED);
    }
}

package com.hhuneau.asobi.game.engine;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.game.Game;
import com.hhuneau.asobi.game.GameType;
import com.hhuneau.asobi.game.player.PlayerService;
import com.hhuneau.asobi.game.pool.Booster;
import com.hhuneau.asobi.websocket.messages.PlayerStateMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.hhuneau.asobi.game.GameType.DRAFT;

@Component
public class DraftEngine implements GameEngine {

    private final CustomerService customerService;
    private PlayerService playerService;

    public DraftEngine(CustomerService customerService, PlayerService playerService) {
        this.customerService = customerService;
        this.playerService = playerService;
    }

    @Override
    @Transactional
    public void start(Game game) {
        game.getPlayers().forEach(player -> {
            final List<Booster> pool = player.getPool();
            if (!pool.isEmpty()) {
                //put the first booster as available
                final Booster firstBooster = pool.get(0);
                player.setRemainingPacks(Collections.singletonList(firstBooster));
                playerService.save(player);
                customerService.send(player.getUserId(), PlayerStateMessage.of(firstBooster));
            }
        });
    }

    @Override
    public boolean isInterested(GameType gameType) {
        return gameType.equals(DRAFT);
    }
}

package com.hhuneau.asobi.game.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhuneau.asobi.game.GameService;
import com.hhuneau.asobi.game.GameType;
import com.hhuneau.asobi.game.player.Player;
import com.hhuneau.asobi.game.pool.Booster;
import com.hhuneau.asobi.game.pool.PoolService;
import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.websocket.messages.PoolMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

import static com.hhuneau.asobi.game.GameType.SEALED;

@Component
public class SealedEngine implements GameEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(SealedEngine.class);
    private final GameService gameService;
    private final ObjectMapper mapper;
    private final PoolService poolService;
    private final CustomerService customerService;

    public SealedEngine(GameService gameService, ObjectMapper mapper, PoolService poolService, CustomerService customerService) {
        this.gameService = gameService;
        this.mapper = mapper;
        this.poolService = poolService;
        this.customerService = customerService;
    }

    @Override
    @Transactional
    //TODO: Refactor as part of Facade or action
    public void start(Long gameId) {
        gameService.getGame(gameId).ifPresent(game -> {
                final Set<Player> players = game.getPlayers();
                //TODO: Responsability of playerService?
                poolService.createPools(players, game);
                gameService.startGame(game);
                //TODO: Responsability of playerService?
                game.getPlayers().forEach(player -> {
                    //getPool
                    final List<Booster> pool = player.getPool();

                    //sendPool via session
                    customerService.send(player.getUserId(), PoolMessage.of(pool));
                });
            }
        );
    }

    @Override
    public boolean isInterested(GameType gameType) {
        return gameType == SEALED;
    }
}

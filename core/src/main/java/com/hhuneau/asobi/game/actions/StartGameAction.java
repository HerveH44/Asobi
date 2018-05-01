package com.hhuneau.asobi.game.actions;

import com.hhuneau.asobi.customer.Customer;
import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.game.GameService;
import com.hhuneau.asobi.game.GameType;
import com.hhuneau.asobi.game.engine.GameEngine;
import com.hhuneau.asobi.game.engine.GameEngineFactory;
import com.hhuneau.asobi.game.pool.PoolService;
import com.hhuneau.asobi.websocket.events.game.StartGameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class StartGameAction implements Action<StartGameEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartGameAction.class);
    private final CustomerService customerService;
    private final GameService gameService;
    private final GameEngineFactory gameEngineFactory;
    private PoolService poolService;

    public StartGameAction(CustomerService customerService, GameService gameService, GameEngineFactory gameEngineFactory, PoolService poolService) {
        this.customerService = customerService;
        this.gameService = gameService;
        this.gameEngineFactory = gameEngineFactory;
        this.poolService = poolService;
    }

    @Override
    @EventListener
    @Transactional
    public void accept(StartGameEvent evt) {
        final Optional<Customer> customer = customerService.find(evt.sessionId);
        if (!customer.isPresent()) {
            LOGGER.error("cannot find session with id {}", evt.sessionId);
            return;
        }
        gameService.getGame(evt.gameId)
            .filter(game -> gameService.canStart(game, evt.authToken))
            .ifPresent(game -> {
                poolService.createPools(game);
                final GameType gameType = game.getGameType();
                final GameEngine engine = gameEngineFactory.getEngine(gameType);
                gameService.startGame(evt.gameId);
                engine.start(game);
            });
    }
}

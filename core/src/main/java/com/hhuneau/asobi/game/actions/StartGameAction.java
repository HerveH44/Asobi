package com.hhuneau.asobi.game.actions;

import com.hhuneau.asobi.customer.Customer;
import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.game.GameService;
import com.hhuneau.asobi.websocket.events.StartGameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StartGameAction implements Action<StartGameEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartGameAction.class);
    private final CustomerService customerService;
    private final GameService gameService;

    public StartGameAction(CustomerService customerService, GameService gameService) {
        this.customerService = customerService;
        this.gameService = gameService;
    }

    @Override
    public void accept(StartGameEvent evt) {
        final Optional<Customer> customer = customerService.find(evt.sessionId);
        if (!customer.isPresent()) {
            LOGGER.error("cannot find session with id {}", evt.sessionId);
            return;
        }
        gameService.startGame(evt.gameId, evt.authToken);
    }
}

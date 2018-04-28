package com.hhuneau.asobi.game.actions.creategame;

import com.hhuneau.asobi.customer.Customer;
import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.game.GameService;
import com.hhuneau.asobi.game.actions.Action;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.messages.GameIdMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CreateGameAction implements Action<CreateGameEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateGameAction.class);
    private final GameService gameService;
    private final CustomerService customerService;

    public CreateGameAction(GameService gameService, CustomerService customerService) {
        this.gameService = gameService;
        this.customerService = customerService;
    }

    @Override
    @EventListener
    public void accept(CreateGameEvent evt) {
        final Optional<Customer> customer = customerService.find(evt.sessionId);
        if (!customer.isPresent()) {
            LOGGER.error("cannot find session with id {}", evt.sessionId);
            return;
        }
        final long gameId = gameService.createGame(evt);
        customerService.send(evt.sessionId, GameIdMessage.of(gameId));
    }
}

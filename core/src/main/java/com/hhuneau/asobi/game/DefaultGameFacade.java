package com.hhuneau.asobi.game;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.game.eventhandler.EventHandler;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.events.game.GameEvent;
import com.hhuneau.asobi.websocket.messages.CreatedGameMessage;
import com.hhuneau.asobi.websocket.messages.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class DefaultGameFacade implements GameFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGameFacade.class);
    private final GameService gameService;
    private final CustomerService customerService;
    private final List<EventHandler> eventHandlers;

    public DefaultGameFacade(GameService gameService, CustomerService customerService, List<EventHandler> eventHandlers) {
        this.gameService = gameService;
        this.customerService = customerService;
        this.eventHandlers = eventHandlers;
    }

    @Override
    @EventListener
    public void handle(CreateGameEvent evt) {
        customerService.find(evt.sessionId)
            .ifPresentOrElse(
                customer -> {
                    final CreateGameDTO createGameDTO = gameService.createGame(evt);
                    customerService.send(evt.sessionId, CreatedGameMessage.of(createGameDTO));
                },
                () -> LOGGER.error("cannot find session with id {}", evt.sessionId)
            );
    }

    @Override
    @EventListener
    public void handle(GameEvent evt) {
        gameService.getGame(evt.gameId)
            .ifPresentOrElse(
                (game) -> {
                    eventHandlers.stream()
                    .filter(eventHandler -> eventHandler.isInterested(game))
                    .forEach(handler -> handler.handle(game, evt));
                },
                () -> customerService.send(evt.sessionId,
                    ErrorMessage.of(String.format("gameId %s does not exist", evt.gameId)))
            );
    }
}

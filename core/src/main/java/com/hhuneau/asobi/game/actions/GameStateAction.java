package com.hhuneau.asobi.game.actions;

import com.hhuneau.asobi.websocket.events.game.GameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class GameStateAction implements Action<GameEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameStateAction.class);

    @Order(99)
    @Override
    @EventListener
    public void accept(GameEvent gameEvent) {
        LOGGER.info("gameState event: {}", gameEvent.type);
    }
}

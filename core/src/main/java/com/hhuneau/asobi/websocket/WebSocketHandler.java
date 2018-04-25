package com.hhuneau.asobi.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhuneau.asobi.game.GameManager;
import com.hhuneau.asobi.websocket.events.Event;
import com.hhuneau.asobi.websocket.events.StartGameEvent;
import com.hhuneau.asobi.websocket.events.server.SessionConnectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class);
    private final ObjectMapper mapper;
    private final GameManager gameManager;
    private final ApplicationEventPublisher publisher;
    private final SessionsRegistry sessionsRegistry;


    public WebSocketHandler(ObjectMapper mapper, GameManager gameManager, ApplicationEventPublisher publisher, SessionsRegistry sessionsRegistry) {
        this.mapper = mapper;
        this.gameManager = gameManager;
        this.publisher = publisher;
        this.sessionsRegistry = sessionsRegistry;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        LOGGER.info("Websocket connected " + session.toString());
        sessionsRegistry.add(new SessionWrapper(session, mapper));
        SessionConnectedEvent sessionConnectedEvent = new SessionConnectedEvent();
        sessionConnectedEvent.sessionId = session.getId();
        publisher.publishEvent(sessionConnectedEvent);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final Event evt = mapper.readValue(message.getPayload(), Event.class);
        evt.sessionId = session.getId();
        publisher.publishEvent(evt);

        switch (evt.type) {
            case LEAVE_GAME:
                LOGGER.info("We got a leave game!");
                break;
            case START_GAME:
                LOGGER.info("We got a game start!");
                gameManager.startGame((StartGameEvent) evt);
                break;
            case ID:
                LOGGER.info("We got an ID");
                break;
            default:
//                throw new UnsupportedOperationException(String.format("unknown event type %s", evt.type));

        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // TODO: Gérer les déconnections en controlant si la socket est rattachée à une partie
        LOGGER.info("Websocket disconnected " + session.toString());
        sessionsRegistry.remove(session.getId());
    }
}

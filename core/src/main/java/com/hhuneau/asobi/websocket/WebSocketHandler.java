package com.hhuneau.asobi.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhuneau.asobi.game.GameManager;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.events.Event;
import com.hhuneau.asobi.websocket.events.JoinGameEvent;
import com.hhuneau.asobi.websocket.events.StartGameEvent;
import com.hhuneau.asobi.websocket.messages.GameIdMessage;
import com.hhuneau.asobi.websocket.states.OnConnectionEstablished;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class);
    private final ObjectMapper mapper;
    private final List<OnConnectionEstablished> onConnectionEstablishedHandlers;
    private final GameManager gameManager;


    public WebSocketHandler(ObjectMapper mapper, List<OnConnectionEstablished> onConnectionEstablishedHandlers, GameManager gameManager) {
        this.mapper = mapper;
        this.onConnectionEstablishedHandlers = onConnectionEstablishedHandlers;
        this.gameManager = gameManager;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        LOGGER.info("Websocket connected " + session.toString());
        onConnectionEstablishedHandlers.forEach(handler -> handler.accept(session));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final Event evt = mapper.readValue(message.getPayload(), Event.class);

        switch (evt.type) {
            case CREATE_GAME:
                final long gameId = gameManager.createGame((CreateGameEvent) evt);
                LOGGER.info("we got a creation game! " + gameId);
                session.sendMessage(new TextMessage(mapper.writeValueAsString(GameIdMessage.of(gameId))));
                break;
            case JOIN_GAME:
                LOGGER.info("We got a join game!");
                gameManager.joinGame(session, (JoinGameEvent) evt);
                break;
            case LEAVE_GAME:
                LOGGER.info("We got a leave game!");
                break;
            case START_GAME:
                LOGGER.info("We got a game start!");
                gameManager.startGame((StartGameEvent) evt);
                break;
            default:
                throw new UnsupportedOperationException(String.format("unknown event type %s", evt.type));

        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // TODO: Gérer les déconnections en controlant si la socket est rattachée à une partie
        LOGGER.info("Websocket disconnected " + session.toString());
    }
}

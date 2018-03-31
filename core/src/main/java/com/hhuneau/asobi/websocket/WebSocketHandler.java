package com.hhuneau.asobi.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhuneau.asobi.websocket.events.Event;
import com.hhuneau.asobi.sets.MTGSetsService;
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


    public WebSocketHandler(ObjectMapper mapper, List<OnConnectionEstablished> onConnectionEstablishedHandlers) {
        this.mapper = mapper;
        this.onConnectionEstablishedHandlers = onConnectionEstablishedHandlers;
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
                LOGGER.info("we got a creationg game!" + evt);
                break;
            default:
                throw new UnsupportedOperationException(String.format("unknown event type %s", evt.type));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        LOGGER.info("Websocket disconnected " + session.toString());
    }
}

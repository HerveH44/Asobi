package com.hhuneau.asobi.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhuneau.asobi.websocket.events.Event;
import com.hhuneau.asobi.websocket.events.SessionConnectedEvent;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class);
    private final ObjectMapper mapper;
    private final ApplicationEventPublisher publisher;
    private final WSCustomerService WSCustomerService;


    public WebSocketHandler(ObjectMapper mapper, ApplicationEventPublisher publisher, WSCustomerService WSCustomerService) {
        this.mapper = mapper;
        this.publisher = publisher;
        this.WSCustomerService = WSCustomerService;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        LOGGER.info("Websocket connected " + session.toString());
        WSCustomerService.add(new WSCustomer(session, mapper));
        SessionConnectedEvent sessionConnectedEvent = new SessionConnectedEvent();
        sessionConnectedEvent.sessionId = session.getId();
        publisher.publishEvent(sessionConnectedEvent);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final Event evt = mapper.readValue(message.getPayload(), Event.class);
        evt.sessionId = session.getId();
        publisher.publishEvent(evt);

//        switch (evt.type) {
//            case LEAVE_GAME:
//                LOGGER.info("We got a leave game!");
//                break;
//            case ID:
//                LOGGER.info("We got an ID");
//                break;
//            default:
//                throw new UnsupportedOperationException(String.format("unknown event type %s", evt.type));
//
//        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // TODO: Gérer les déconnections en controlant si la socket est rattachée à une partie
        LOGGER.info("Websocket disconnected " + session.toString());
        WSCustomerService.remove(session.getId());
    }
}

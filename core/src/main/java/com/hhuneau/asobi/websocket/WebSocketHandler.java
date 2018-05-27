package com.hhuneau.asobi.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhuneau.asobi.customer.CustomerService;
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
    private final CustomerService customerService;


    public WebSocketHandler(ObjectMapper mapper, ApplicationEventPublisher publisher, CustomerService customerService) {
        this.mapper = mapper;
        this.publisher = publisher;
        this.customerService = customerService;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        customerService.add(new WSCustomer(session, mapper));
        SessionConnectedEvent sessionConnectedEvent = new SessionConnectedEvent();
        sessionConnectedEvent.sessionId = session.getId();
        publisher.publishEvent(sessionConnectedEvent);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final Event evt = mapper.readValue(message.getPayload(), Event.class);
        evt.sessionId = session.getId();
        publisher.publishEvent(evt);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // TODO: Gérer les déconnections en controlant si la socket est rattachée à une partie
        customerService.remove(session.getId());
    }
}

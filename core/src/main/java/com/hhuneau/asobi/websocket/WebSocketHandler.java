package com.hhuneau.asobi.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.websocket.events.Event;
import com.hhuneau.asobi.websocket.events.SessionConnectedEvent;
import com.hhuneau.asobi.websocket.events.SessionDisconnectedEvent;
import com.hhuneau.asobi.websocket.messages.ErrorMessage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.UUID;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

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
        final String sessionId = UUID.randomUUID().toString();
        customerService.add(new WSCustomer(session, sessionId, mapper));
        session.getAttributes().put("sessionId", sessionId);
        SessionConnectedEvent sessionConnectedEvent = new SessionConnectedEvent();
        sessionConnectedEvent.sessionId = sessionId;
        publisher.publishEvent(sessionConnectedEvent);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final Event evt = mapper.readValue(message.getPayload(), Event.class);
        evt.sessionId = getSessionId(session);
        try {
            publisher.publishEvent(evt);
        } catch (Exception e) {
            customerService.send(getSessionId(session), ErrorMessage.of(e.getMessage()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        final String sessionId = getSessionId(session);
        customerService.remove(sessionId);
        SessionDisconnectedEvent sessionConnectedEvent = SessionDisconnectedEvent.of(sessionId);
        publisher.publishEvent(sessionConnectedEvent);
    }

    private String getSessionId(WebSocketSession session) {
        return (String) session.getAttributes().getOrDefault("sessionId", "");
    }
}

package com.hhuneau.asobi.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhuneau.asobi.customer.Customer;
import com.hhuneau.asobi.websocket.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class WSCustomer implements Customer {
    private static final Logger LOGGER = LoggerFactory.getLogger(WSCustomer.class);
    private final String sessionId;
    private final WebSocketSession session;
    private final ObjectMapper mapper;

    WSCustomer(WebSocketSession session, String sessionId, ObjectMapper mapper) {
        this.session = session;
        this.sessionId = sessionId;
        this.mapper = mapper;
    }

    @Override
    public void send(Message message) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            LOGGER.error("Error while sending message", e);
        }
    }

    @Override
    public String getId() {
        return sessionId;
    }
}

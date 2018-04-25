package com.hhuneau.asobi.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhuneau.asobi.websocket.events.JoinGameEvent;
import com.hhuneau.asobi.websocket.events.PlayerIdEvent;
import com.hhuneau.asobi.websocket.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class SessionWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionWrapper.class);
    private final String sessionId;
    private final WebSocketSession session;
    private final ObjectMapper mapper;
    private long gameId;
    private long playerId;

    SessionWrapper(WebSocketSession session, ObjectMapper mapper) {
        this.session = session;
        this.sessionId = session.getId();
        this.mapper = mapper;
    }

    public void onJoinedGameEvent(JoinGameEvent event) {
        gameId = event.gameId;
    }

    public void onPlayerIdEvent(PlayerIdEvent event) {
        playerId = event.playerId;
    }

    public void send(Message message) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            LOGGER.error("Error while sending message", e);
        }
    }

    public String getId() {
        return sessionId;
    }
}

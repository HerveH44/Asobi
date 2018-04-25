package com.hhuneau.asobi.websocket;

import com.hhuneau.asobi.websocket.events.JoinGameEvent;
import com.hhuneau.asobi.websocket.events.PlayerIdEvent;
import com.hhuneau.asobi.websocket.events.server.SessionMessageEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SessionsRegistry {

    private final Map<String, SessionWrapper> map = new HashMap<>();

    public void add(SessionWrapper session) {
        map.put(session.getId(), session);
    }

    public void remove(String sessionId) {
        map.remove(sessionId);
    }

    @EventListener
    public void onSessionMessageEvent(SessionMessageEvent event) {
        final SessionWrapper sessionWrapper = map.getOrDefault(event.sessionId, null);

        if (sessionWrapper != null) {
            sessionWrapper.send(event.message);
        }
    }

    @EventListener
    public void onJoinedGameEvent(JoinGameEvent event) {
        final SessionWrapper sessionWrapper = map.getOrDefault(event.sessionId, null);
        if (sessionWrapper != null) {
            sessionWrapper.onJoinedGameEvent(event);
        }
    }

    @EventListener
    public void onPlayerIdEvent(PlayerIdEvent event) {
        final SessionWrapper sessionWrapper = map.getOrDefault(event.sessionId, null);
        if (sessionWrapper != null) {
            sessionWrapper.onPlayerIdEvent(event);
        }
    }

}

package com.hhuneau.asobi.websocket.events;

public class SessionDisconnectedEvent extends Event {
    public static SessionDisconnectedEvent of(String sessionId) {
        final SessionDisconnectedEvent event = new SessionDisconnectedEvent();
        event.sessionId = sessionId;
        return event;
    }
}

package com.hhuneau.asobi.websocket.events.server;

import com.hhuneau.asobi.websocket.events.Event;

abstract class ServerEvent extends Event {
    public String sessionId;
}

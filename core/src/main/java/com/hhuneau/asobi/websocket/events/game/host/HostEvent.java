package com.hhuneau.asobi.websocket.events.game.host;

import com.hhuneau.asobi.websocket.events.game.GameEvent;

public abstract class HostEvent extends GameEvent {
    public String authToken;
}

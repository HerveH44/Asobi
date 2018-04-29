package com.hhuneau.asobi.websocket.events.game.player;

import com.hhuneau.asobi.websocket.events.game.GameEvent;

public abstract class PlayerEvent extends GameEvent {
    public long playerId;
}

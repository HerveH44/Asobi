package com.hhuneau.asobi.websocket.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.hhuneau.asobi.websocket.events.game.host.KickPlayerEvent;
import com.hhuneau.asobi.websocket.events.game.host.StartGameEvent;
import com.hhuneau.asobi.websocket.events.game.host.SwapPlayerEvent;
import com.hhuneau.asobi.websocket.events.game.player.*;

@JsonTypeInfo(visible = true, use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(value = {
    @JsonSubTypes.Type(value = CreateGameEvent.class, name = "CREATE_GAME"),
    @JsonSubTypes.Type(value = JoinGameEvent.class, name = "JOIN_GAME"),
    @JsonSubTypes.Type(value = LeaveGameEvent.class, name = "LEAVE_GAME"),
    @JsonSubTypes.Type(value = StartGameEvent.class, name = "START_GAME"),
    @JsonSubTypes.Type(value = PickEvent.class, name = "PICK"),
    @JsonSubTypes.Type(value = AutoPickEvent.class, name = "AUTOPICK"),
    @JsonSubTypes.Type(value = KickPlayerEvent.class, name = "KICK"),
    @JsonSubTypes.Type(value = SwapPlayerEvent.class, name = "SWAP"),
    @JsonSubTypes.Type(value = HashDeckEvent.class, name = "HASH"),
})
public abstract class Event {
    public EventType type;
    public String sessionId;
}

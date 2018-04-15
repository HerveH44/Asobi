package com.hhuneau.asobi.websocket.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(visible = true, use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(value = {
    @JsonSubTypes.Type(value = CreateGameEvent.class, name = "CREATE_GAME"),
    @JsonSubTypes.Type(value = JoinGameEvent.class, name = "JOIN_GAME"),
    @JsonSubTypes.Type(value = LeaveGameEvent.class, name = "LEAVE_GAME"),
    @JsonSubTypes.Type(value = StartGameEvent.class, name = "START_GAME")
})
public abstract class Event {
    public EventType type;
}

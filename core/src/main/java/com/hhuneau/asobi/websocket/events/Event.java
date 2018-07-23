package com.hhuneau.asobi.websocket.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.hhuneau.asobi.websocket.events.game.StartGameEvent;
import com.hhuneau.asobi.websocket.events.game.player.AutoPickEvent;
import com.hhuneau.asobi.websocket.events.game.player.JoinGameEvent;
import com.hhuneau.asobi.websocket.events.game.player.LeaveGameEvent;
import com.hhuneau.asobi.websocket.events.game.player.PickEvent;

@JsonTypeInfo(visible = true, use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(value = {
    @JsonSubTypes.Type(value = CreateGameEvent.class, name = "CREATE_GAME"),
    @JsonSubTypes.Type(value = JoinGameEvent.class, name = "JOIN_GAME"),
    @JsonSubTypes.Type(value = LeaveGameEvent.class, name = "LEAVE_GAME"),
    @JsonSubTypes.Type(value = StartGameEvent.class, name = "START_GAME"),
    @JsonSubTypes.Type(value = PickEvent.class, name = "PICK"),
    @JsonSubTypes.Type(value = AutoPickEvent.class, name = "AUTOPICK"),
})
public abstract class Event {
    public EventType type;
    public String sessionId;
}

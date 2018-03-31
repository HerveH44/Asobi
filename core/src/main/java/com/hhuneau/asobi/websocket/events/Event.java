package com.hhuneau.asobi.websocket.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(visible = true, use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(value = {
  @JsonSubTypes.Type(value = CreateGameEvent.class, name = "CREATE_GAME")
})
public abstract class Event {

  public EventType type;
}

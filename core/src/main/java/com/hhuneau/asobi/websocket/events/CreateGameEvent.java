package com.hhuneau.asobi.websocket.events;

public class CreateGameEvent extends Event {
    public String title;
    public long seats;
    public boolean isPrivate;
    /* TODO:  Enum? */
    public String gameType;
    public String gameMode;
}

package com.hhuneau.asobi.websocket.events;

import com.hhuneau.asobi.game.GameMode;
import com.hhuneau.asobi.game.GameType;

public class CreateGameEvent extends Event {
    public String title;
    public long seats;
    public boolean isPrivate;
    public GameType gameType;
    public GameMode gameMode;
}

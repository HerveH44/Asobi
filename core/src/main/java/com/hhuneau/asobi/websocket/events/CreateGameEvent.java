package com.hhuneau.asobi.websocket.events;

import com.hhuneau.asobi.mtg.game.GameMode;
import com.hhuneau.asobi.mtg.game.GameType;

import java.util.List;

public class CreateGameEvent extends Event {
    public String title;
    public long seats;
    public boolean isPrivate;
    public GameType gameType;
    public GameMode gameMode;
    public List<String> sets;
}

package com.hhuneau.asobi.websocket.events.game;

import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.eventhandler.EventHandler;
import com.hhuneau.asobi.mtg.game.Timer;

public class StartGameEvent extends GameEvent {
    public String authToken;
    public boolean useTimer;
    public boolean addBots;
    public boolean shufflePlayers;
    public Timer timerLength;

    @Override
    public void accepts(Game game, EventHandler eventHandler) {
        eventHandler.handle(game, this);
    }
}

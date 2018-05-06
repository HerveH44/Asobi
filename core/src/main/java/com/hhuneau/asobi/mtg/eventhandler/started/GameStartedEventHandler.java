package com.hhuneau.asobi.mtg.eventhandler.started;

import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.eventhandler.EventHandler;
import com.hhuneau.asobi.websocket.events.game.StartGameEvent;
import com.hhuneau.asobi.websocket.events.game.player.JoinGameEvent;
import com.hhuneau.asobi.websocket.events.game.player.LeaveGameEvent;

import static com.hhuneau.asobi.mtg.game.Status.STARTED;

public abstract class GameStartedEventHandler implements EventHandler {
    @Override
    public void handle(Game game, JoinGameEvent evt) {

    }

    @Override
    public void handle(Game game, LeaveGameEvent evt) {

    }

    @Override
    public void handle(Game game, StartGameEvent evt) {

    }

    @Override
    public boolean isInterested(Game game) {
        return game.getStatus().equals(STARTED);
    }
}

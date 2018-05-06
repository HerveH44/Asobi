package com.hhuneau.asobi.mtg.eventhandler;

import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.websocket.events.game.GameEvent;
import com.hhuneau.asobi.websocket.events.game.StartGameEvent;
import com.hhuneau.asobi.websocket.events.game.player.JoinGameEvent;
import com.hhuneau.asobi.websocket.events.game.player.LeaveGameEvent;

public interface EventHandler {
    void handle(Game game, JoinGameEvent evt);
    void handle(Game game, LeaveGameEvent evt);
    void handle(Game game, StartGameEvent evt);
    boolean isInterested(Game game);

    default void handle(Game game, GameEvent evt) {
        evt.accepts(game, this);
    }
}

package com.hhuneau.asobi.mtg.eventhandler;

import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.websocket.events.game.GameEvent;
import com.hhuneau.asobi.websocket.events.game.StartGameEvent;
import com.hhuneau.asobi.websocket.events.game.player.AutoPickEvent;
import com.hhuneau.asobi.websocket.events.game.player.JoinGameEvent;
import com.hhuneau.asobi.websocket.events.game.player.LeaveGameEvent;
import com.hhuneau.asobi.websocket.events.game.player.PickEvent;

public interface EventHandler {

    default void handle(Game game, JoinGameEvent evt) {

    }

    default void handle(Game game, LeaveGameEvent evt) {

    }

    default void handle(Game game, StartGameEvent evt) {

    }

    default void handle(Game game, PickEvent evt) {

    }

    default void handle(Game game, AutoPickEvent evt) {

    }

    boolean isInterested(Game game);

    default void handle(Game game, GameEvent evt) {
        evt.accepts(game, this);
    }
}

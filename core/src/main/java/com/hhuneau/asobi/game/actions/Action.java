package com.hhuneau.asobi.game.actions;

import com.hhuneau.asobi.websocket.events.Event;

import java.util.function.Consumer;

interface Action<T extends Event> extends Consumer<T> {
}

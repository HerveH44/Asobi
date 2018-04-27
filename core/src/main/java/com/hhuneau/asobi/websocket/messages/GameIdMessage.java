package com.hhuneau.asobi.websocket.messages;

public class GameIdMessage extends Message {

    public final String type = "GAME_ID";

    public static GameIdMessage of(long gameId) {
        final GameIdMessage o = new GameIdMessage();
        o.setPayload(gameId);
        return o;
    }
}

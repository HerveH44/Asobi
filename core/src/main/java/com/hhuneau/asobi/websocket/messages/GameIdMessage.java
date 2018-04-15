package com.hhuneau.asobi.websocket.messages;

public class GameIdMessage extends Message {

    public static GameIdMessage of(long gameId) {
        final GameIdMessage o = new GameIdMessage();
        o.setType("GAME_ID");
        o.setPayload(gameId);
        return o;
    }
}

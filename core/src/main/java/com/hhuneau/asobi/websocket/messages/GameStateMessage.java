package com.hhuneau.asobi.websocket.messages;

import java.util.Map;

public class GameStateMessage extends Message {
    public static GameStateMessage of(Map<String, Integer> gameState) {
        final GameStateMessage message = new GameStateMessage();
        message.setType("GAME_STATE");
        message.setPayload(gameState);
        return message;
    }
}

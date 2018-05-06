package com.hhuneau.asobi.websocket.messages;

public class GameStateMessage extends Message {
    public static GameStateMessage of(Object gameState) {
        final GameStateMessage message = new GameStateMessage();
        message.setType("GAME_STATE");
        message.setPayload(gameState);
        return message;
    }
}

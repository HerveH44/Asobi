package com.hhuneau.asobi.websocket.messages;

public class PlayerStateMessage extends Message {
    public static PlayerStateMessage of(Object state) {
        final PlayerStateMessage message = new PlayerStateMessage();
        message.setType("PLAYER_STATE");
        message.setPayload(state);
        return message;
    }
}

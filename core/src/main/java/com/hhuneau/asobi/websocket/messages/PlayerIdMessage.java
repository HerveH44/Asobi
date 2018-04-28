package com.hhuneau.asobi.websocket.messages;

public class PlayerIdMessage extends Message {

    public static PlayerIdMessage of(long playerId) {
        final PlayerIdMessage message = new PlayerIdMessage();
        message.setPayload(playerId);
        message.setType("PLAYER_ID");
        return message;
    }
}

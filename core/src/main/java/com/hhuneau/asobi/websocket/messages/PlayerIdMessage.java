package com.hhuneau.asobi.websocket.messages;

public class PlayerIdMessage extends Message {

    public final String type = "PLAYER_ID";

    public static PlayerIdMessage of(long playerId) {
        final PlayerIdMessage message = new PlayerIdMessage();
        message.setPayload(playerId);
        return message;
    }
}

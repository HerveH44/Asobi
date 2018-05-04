package com.hhuneau.asobi.websocket.messages;

import com.hhuneau.asobi.game.pool.Booster;

public class PlayerStateMessage extends Message {
    public static PlayerStateMessage of(Booster booster) {
        final PlayerStateMessage message = new PlayerStateMessage();
        message.setType("PLAYER_STATE");
        message.setPayload(booster);
        return message;
    }
}

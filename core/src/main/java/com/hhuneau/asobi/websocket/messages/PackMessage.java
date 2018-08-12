package com.hhuneau.asobi.websocket.messages;

import com.hhuneau.asobi.mtg.pool.Pack;

public class PackMessage extends Message {

    public static PackMessage of(Pack pack) {
        final PackMessage message = new PackMessage();
        message.setType("PACK");
        message.setPayload(pack);
        return message;
    }
}

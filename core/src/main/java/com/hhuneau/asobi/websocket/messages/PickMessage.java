package com.hhuneau.asobi.websocket.messages;

import com.hhuneau.asobi.mtg.sets.MTGCard;

public class PickMessage extends Message {

    public static PickMessage of(MTGCard card) {
        final PickMessage message = new PickMessage();
        message.setType("PICKED_CARD");
        message.setPayload(card);
        return message;
    }
}

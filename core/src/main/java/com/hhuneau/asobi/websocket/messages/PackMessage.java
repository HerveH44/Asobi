package com.hhuneau.asobi.websocket.messages;

import com.hhuneau.asobi.mtg.sets.MTGCard;

import java.util.List;
import java.util.Set;

public class PackMessage extends Message {

    public static PackMessage of(List<MTGCard> cards) {
        final PackMessage message = new PackMessage();
        message.setType("PACK");
        message.setPayload(cards);
        return message;
    }
}

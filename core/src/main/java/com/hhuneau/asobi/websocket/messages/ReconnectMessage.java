package com.hhuneau.asobi.websocket.messages;

import com.hhuneau.asobi.mtg.sets.MTGCard;

import java.util.List;

public class ReconnectMessage extends Message {
    public static ReconnectMessage of(List<MTGCard> cards) {
        final ReconnectMessage message = new ReconnectMessage();
        message.setType("RECONNECT");
        message.setPayload(cards);
        return message;
    }
}

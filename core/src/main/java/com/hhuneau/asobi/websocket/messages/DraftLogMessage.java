package com.hhuneau.asobi.websocket.messages;

import com.hhuneau.asobi.mtg.player.Pick;

import java.util.List;

public class DraftLogMessage extends Message {

    public static DraftLogMessage of(List<Pick> picks) {
        final DraftLogMessage o = new DraftLogMessage();
        o.setType("DRAFT_LOG");
        o.setPayload(picks);
        return o;
    }

}

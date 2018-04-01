package com.hhuneau.asobi.websocket.messages;

public class SetsExportMessage extends Message {

    public static SetsExportMessage of(Object payload) {
        final SetsExportMessage o = new SetsExportMessage();
        o.setType("IMPORT_SETS");
        o.setPayload(payload);
        return o;
    }
}

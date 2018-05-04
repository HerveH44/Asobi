package com.hhuneau.asobi.websocket.messages;

public abstract class Message {
    private String type;
    private Object payload;

    public String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }

    public Object getPayload() {
        return payload;
    }

    void setPayload(Object payload) {
        this.payload = payload;
    }
}

package com.hhuneau.asobi.websocket.messages;

public class ErrorMessage extends Message {

    public final String type = "ERROR";

    public static ErrorMessage of(String error) {
        final ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setPayload(error);
        return errorMessage;
    }
}

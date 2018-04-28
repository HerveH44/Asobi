package com.hhuneau.asobi.websocket.messages;

public class ErrorMessage extends Message {

    public static ErrorMessage of(String error) {
        final ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setPayload(error);
        errorMessage.setType("ERROR");
        return errorMessage;
    }
}

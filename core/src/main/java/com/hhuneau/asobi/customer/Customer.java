package com.hhuneau.asobi.customer;

import com.hhuneau.asobi.websocket.messages.Message;

public interface Customer {

    void send(Message message);

    String getId();
}

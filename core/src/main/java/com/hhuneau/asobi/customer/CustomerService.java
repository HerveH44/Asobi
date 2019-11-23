package com.hhuneau.asobi.customer;

import com.hhuneau.asobi.websocket.messages.Message;

public interface CustomerService {

    void send(String customerId, Message message);

    void broadcast(String destination, Object message);
}

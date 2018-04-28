package com.hhuneau.asobi.websocket;

import com.hhuneau.asobi.websocket.messages.Message;

import java.util.Optional;

public interface CustomerService {

    void add(Customer customer);
    void remove(String customerId);

    Optional<Customer> find(String sessionId);

    void send(String customerId, Message message);
}

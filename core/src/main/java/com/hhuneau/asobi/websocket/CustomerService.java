package com.hhuneau.asobi.websocket;

import java.util.Optional;

public interface CustomerService {

    void add(Customer customer);
    void remove(String customerId);

    Optional<Customer> find(String sessionId);
}

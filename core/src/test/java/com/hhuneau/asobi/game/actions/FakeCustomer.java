package com.hhuneau.asobi.game.actions;

import com.hhuneau.asobi.customer.Customer;
import com.hhuneau.asobi.websocket.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FakeCustomer implements Customer {

    private final String id;
    private static final Logger LOGGER = LoggerFactory.getLogger(FakeCustomer.class);

    public FakeCustomer(String id) {
        this.id = id;
    }

    @Override
    public void send(Message message) {
        LOGGER.info("[{}] received message {}", id ,message.getType());
    }

    @Override
    public String getId() {
        return id;
    }
}

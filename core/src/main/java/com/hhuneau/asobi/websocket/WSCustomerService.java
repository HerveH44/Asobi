package com.hhuneau.asobi.websocket;

import com.hhuneau.asobi.websocket.events.server.SessionMessageEvent;
import com.hhuneau.asobi.websocket.messages.Message;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class WSCustomerService implements CustomerService {

    private final Map<String, Customer> map = new HashMap<>();

    public void add(Customer session) {
        map.put(session.getId(), session);
    }

    @Override
    public void remove(String sessionId) {
        map.remove(sessionId);
    }

    @Override
    public Optional<Customer> find(String sessionId) {
        final Customer customer = map.getOrDefault(sessionId, null);
        return customer != null ? Optional.of(customer) : Optional.empty();
    }

    @Override
    public void send(String customerId, Message message) {
        find(customerId).ifPresent(customer -> customer.send(message));
    }

    @EventListener
    @Deprecated
    //TODO: delete method and Event
    public void onSessionMessageEvent(SessionMessageEvent event) {
        find(event.sessionId).ifPresent(customer -> customer.send(event.message));
    }
}

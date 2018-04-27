package com.hhuneau.asobi.websocket;

import com.hhuneau.asobi.websocket.events.server.SessionMessageEvent;
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

    @EventListener
    public void onSessionMessageEvent(SessionMessageEvent event) {
        find(event.sessionId).ifPresent(customer -> customer.send(event.message));
    }

//    @EventListener
//    public void onJoinedGameEvent(JoinGameEvent event) {
//        final Customer sessionWrapper = map.getOrDefault(event.sessionId, null);
//        if (sessionWrapper != null) {
//            sessionWrapper.onJoinedGameEvent(event);
//        }
//    }
//
//    @EventListener
//    public void onPlayerIdEvent(PlayerIdEvent event) {
//        final Customer sessionWrapper = map.getOrDefault(event.sessionId, null);
//        if (sessionWrapper != null) {
//            sessionWrapper.onPlayerIdEvent(event);
//        }
//    }

}

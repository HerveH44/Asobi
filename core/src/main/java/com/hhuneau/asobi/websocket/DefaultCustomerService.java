package com.hhuneau.asobi.websocket;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.websocket.messages.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class DefaultCustomerService implements CustomerService {

    private final SimpMessagingTemplate messagingTemplate;

    public DefaultCustomerService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void send(String customerId, Message message) {
        messagingTemplate.convertAndSendToUser(
            customerId,
            "/queue/reply",
            message,
            createHeaders(customerId));
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

    @Override
    public void broadcast(String destination, Object message) {
        messagingTemplate.convertAndSend(destination, message);
    }
}

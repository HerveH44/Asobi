package com.hhuneau.asobi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhuneau.asobi.websocket.events.Event;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.io.IOException;

@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final ObjectMapper objectMapper;

    public StompWebSocketConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        final HttpSessionHandshakeInterceptor httpSessionHandshakeInterceptor = new HttpSessionHandshakeInterceptor();
        httpSessionHandshakeInterceptor.setCreateSession(true);
        registry.addEndpoint("/stomp")
            .addInterceptors(httpSessionHandshakeInterceptor)
            .setAllowedOrigins("*")
            .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                final StompHeaderAccessor accessor =
                    MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.SEND.equals(accessor.getCommand())) {
                    byte[] bytes = (byte[]) message.getPayload();
//                    MimeType mimeType = accessor.getContentType();
//                    Charset charset = mimeType.getCharset();
//                    charset = (charset != null ? charset : StandardCharsets.UTF_8);
//                    final String realPayload = new String(bytes, charset);
                    try {
                        final Event event = objectMapper.readValue(bytes, Event.class);
                        event.sessionId = accessor.getSessionId();
                        return new GenericMessage<>(objectMapper.writeValueAsBytes(event), message.getHeaders());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //accessor.getLogin ou accessor.getPasscode ou accessor.getNativeHeaders ...
//                    final UserDetails userDetails = User.withUsername("login")
//                        .password("password")
//                        .accountExpired(false)
//                        .build();
//                    final UsernamePasswordAuthenticationToken authenticationToken =
//                        new UsernamePasswordAuthenticationToken(userDetails, "password", userDetails.getAuthorities());
//                    accessor.setUser(authenticationToken);
                }
                return message;
            }
        });
    }
}

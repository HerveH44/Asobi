package com.hhuneau.asobi.websocket.states;

import org.springframework.web.socket.WebSocketSession;

import java.util.function.Consumer;

public interface OnConnectionEstablished extends Consumer<WebSocketSession> {

}

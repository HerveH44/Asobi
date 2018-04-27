package com.hhuneau.asobi.game;

import com.hhuneau.asobi.game.player.Player;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PlayerSessionService {
    private final Map<Long, WebSocketSession> map = new ConcurrentHashMap<>();

    public void add(Player player, WebSocketSession session) {
        map.put(player.getPlayerId(), session);
    }

    public WebSocketSession getSession(Player player) {
        return map.get(player.getPlayerId());
    }
}

package com.hhuneau.asobi.websocket.messages;

public class LobbyStatsMessage extends Message {
    public static LobbyStatsMessage of(Object lobbyStats) {
        final LobbyStatsMessage message = new LobbyStatsMessage();
        message.setType("LOBBY_STATS");
        message.setPayload(lobbyStats);
        return message;
    }
}

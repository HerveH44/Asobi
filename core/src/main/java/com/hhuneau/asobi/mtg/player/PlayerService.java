package com.hhuneau.asobi.mtg.player;

public interface PlayerService {

    void save(Player player);

    void disconnectPlayersWithSession(String sessionId);
}

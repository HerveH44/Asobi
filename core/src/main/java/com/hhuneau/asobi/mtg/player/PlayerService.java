package com.hhuneau.asobi.mtg.player;

import java.util.List;

public interface PlayerService {

    void save(Player player);

    void disconnectPlayersWithSession(String sessionId);

    void hashDeck(Player player, List<String> main, List<String> side);
}

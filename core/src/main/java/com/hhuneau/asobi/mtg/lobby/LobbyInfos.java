package com.hhuneau.asobi.mtg.lobby;

import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hhuneau.asobi.mtg.game.Status.CREATED;
import static com.hhuneau.asobi.mtg.game.Status.STARTED;

@Component
@Transactional
public class LobbyInfos implements ApplicationStatistics {

    private final GameService gameService;
    private long connectedUsers;

    public LobbyInfos(GameService gameService) {
        this.gameService = gameService;
    }

    @EventListener
    public void handle(SessionConnectedEvent evt) {
        connectedUsers = connectedUsers +1;
    }

    @EventListener
    public void handle(SessionDisconnectEvent evt) {
        connectedUsers = connectedUsers -1;
    }

    @Override
    public LobbyStatsDTO getStatistics() {
        final List<Game> createdGames = gameService.getGamesByStatus(CREATED);
        final List<Game> allCurrentGames = gameService.getGamesByStatus(STARTED);
        allCurrentGames.addAll(createdGames);
        final int games = allCurrentGames.size();
        final long players = allCurrentGames.stream()
            .map(Game::getPlayers)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .filter(player -> !player.isBot())
            .count();
        final List<RoomInfo> roomsInfo = createdGames.stream()
            .filter(game -> !game.isPrivate())
            .map(RoomInfo::of)
            .collect(Collectors.toList());

        return LobbyStatsDTO.of(games, players, connectedUsers, roomsInfo);
    }
}

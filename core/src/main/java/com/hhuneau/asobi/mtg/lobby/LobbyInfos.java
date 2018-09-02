package com.hhuneau.asobi.mtg.lobby;

import com.hhuneau.asobi.customer.CustomerService;
import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameService;
import com.hhuneau.asobi.mtg.game.GameType;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import com.hhuneau.asobi.websocket.events.SessionConnectedEvent;
import com.hhuneau.asobi.websocket.events.SessionDisconnectedEvent;
import com.hhuneau.asobi.websocket.events.game.player.JoinGameEvent;
import com.hhuneau.asobi.websocket.messages.LobbyStatsMessage;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hhuneau.asobi.mtg.game.Status.CREATED;
import static com.hhuneau.asobi.mtg.game.Status.STARTED;

@Component
@Transactional
public class LobbyInfos {

    private CustomerService customerService;
    private List<String> customersId = new ArrayList<>();
    private GameService gameService;

    private long players;
    private long users;
    private int games;
    private List<RoomInfo> roomsInfo;

    public LobbyInfos(CustomerService customerService, GameService gameService) {
        this.customerService = customerService;
        this.gameService = gameService;
    }

    @EventListener
    public void handle(SessionConnectedEvent evt) {
        customersId.add(evt.sessionId);
        broadcastLobbyInfos();
    }

    @EventListener
    public void handle(SessionDisconnectedEvent evt) {
        customersId.removeIf(sessionId -> sessionId.equals(evt.sessionId));
        broadcastLobbyInfos();
    }

    @EventListener
    public void handle(CreateGameEvent evt) {
        customersId.removeIf(sessionId -> sessionId.equals(evt.sessionId));
        broadcastLobbyInfos();
    }

    @EventListener
    public void handle(JoinGameEvent evt) {
        customersId.removeIf(sessionId -> sessionId.equals(evt.sessionId));
        broadcastLobbyInfos();
    }

    private void broadcastLobbyInfos() {
        final List<Game> createdGames = gameService.getGamesByStatus(CREATED);
        final List<Game> allCurrentGames = gameService.getGamesByStatus(STARTED);
        allCurrentGames.addAll(createdGames);

        games = allCurrentGames.size();
        players = allCurrentGames.stream()
            .map(Game::getPlayers)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .filter(player -> !player.isBot())
            .count();
        users = customersId.size() + players;
        roomsInfo = createdGames.stream()
            .filter(game -> !game.isPrivate())
            .map(RoomInfo::of)
            .collect(Collectors.toList());

        final LobbyStatsDTO stats = LobbyStatsDTO.of(games, players, users, roomsInfo);
        final LobbyStatsMessage message = LobbyStatsMessage.of(stats);
        customersId.forEach(sessionId -> customerService.send(sessionId, message));
    }

    public static class LobbyStatsDTO {
        public long players;
        public long users;
        public int games;
        public List<RoomInfo> roomsInfo;

        public static LobbyStatsDTO of(int games, long players, long users, List<RoomInfo> roomsInfo) {
            final LobbyStatsDTO stats = new LobbyStatsDTO();
            stats.games = games;
            stats.players = players;
            stats.users = users;
            stats.roomsInfo = roomsInfo;
            return stats;
        }
    }

    public static class RoomInfo {
        public long id;
        public String title;
        public GameType type;
        public String packsInfo;
        public int usedSeats;
        public long totalSeats;

        public static RoomInfo of(Game game) {
            final RoomInfo info = new RoomInfo();
            info.id = game.getGameId();
            info.title = game.getTitle();
            info.type = game.getGameType();
            info.packsInfo = game.getPacksInfo();
            info.totalSeats = game.getSeats();
            info.usedSeats = game.getPlayers().size();
            return info;
        }
    }
}

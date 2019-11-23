package com.hhuneau.asobi.mtg.lobby;

import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameType;

import java.util.List;

public interface ApplicationStatistics {
    LobbyStatsDTO getStatistics();

    class LobbyStatsDTO {
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

    class RoomInfo {
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

package com.hhuneau.asobi.mtg.game;

import com.hhuneau.asobi.mtg.player.Player;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.hhuneau.asobi.mtg.game.Status.CREATED;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;

@Entity
@Getter
@Setter
public class Game {
    @Id
    @GeneratedValue
    private long gameId;
    private String title;
    private long seats;
    private boolean isPrivate;
    @Enumerated(STRING)
    private GameMode gameMode;
    @Enumerated(STRING)
    private GameType gameType;
    @Enumerated(STRING)
    private Status status = CREATED;
    private Date createdDate = new Date();
    private String authToken;
    private int round;
    private Timer timer;
    private boolean useTimer;
    private String hostId;
    private boolean totalChaos;
    private boolean modernOnly;
    private int packsNumber;

    @ManyToMany
    @JoinColumn(name = "cards_ids")
    private List<MTGCard> cubeList;

    @OneToMany(orphanRemoval = true, cascade = ALL)
    @JoinColumn(name = "game_id")
    private List<Player> players;

    @ManyToMany
    @JoinColumn(name = "sets_list")
    private List<MTGSet> sets;

    @OneToMany(orphanRemoval = true, mappedBy = "game", cascade = ALL)
    private List<GameMessage> messages;

    public static Game of(CreateGameEvent evt, List<MTGSet> sets, String authToken, List<MTGCard> cubeList) {
        final Game game = new Game();
        game.setTitle(evt.title);
        game.setSeats(evt.seats);
        game.setPrivate(evt.isPrivate);
        game.setGameMode(evt.gameMode);
        game.setGameType(evt.gameType);
        game.setSets(sets);
        game.setAuthToken(authToken);
        game.setHostId(evt.sessionId);
        game.setModernOnly(evt.modernOnly);
        game.setTotalChaos(evt.totalChaos);
        game.setPacksNumber(evt.packsNumber);
        game.setCubeList(cubeList);
        return game;
    }

    public boolean isFull() {
        return players.size() >= seats;
    }

    public String getPacksInfo() {
        final String packsInfo = String.format("%s %s - ", getGameMode(), getGameType());
        switch (getGameMode()) {
            case NORMAL:
                return packsInfo + String.join("-", getSets().stream().map(MTGSet::getCode).collect(Collectors.toList()));
            case CUBE:
                return packsInfo + getCubeList().size() + " cards";
            case CHAOS:
                return packsInfo + String.format("%s %s", isTotalChaos() ? "Total Chaos " : "", isModernOnly() ? "Modern sets only" : "");
        }
        return packsInfo;
    }
}

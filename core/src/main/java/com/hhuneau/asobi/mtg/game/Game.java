package com.hhuneau.asobi.mtg.game;

import com.hhuneau.asobi.mtg.player.Player;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

    @OneToMany(orphanRemoval = true, cascade = ALL)
    @JoinColumn(name = "game_id")
    private Set<Player> players;
    @ManyToMany
    @JoinColumn(name = "sets_list")
    private List<MTGSet> sets;

    public static Game of(String title,
                          long seats,
                          boolean isPrivate,
                          GameMode gameMode,
                          GameType gameType,
                          List<MTGSet> sets,
                          String authToken) {
        final Game game = new Game();
        game.setTitle(title);
        game.setSeats(seats);
        game.setPrivate(isPrivate);
        game.setGameMode(gameMode);
        game.setGameType(gameType);
        game.setSets(sets);
        game.setAuthToken(authToken);
        return game;
    }

    public boolean isFull() {
        return seats <= players.size();
    }
}

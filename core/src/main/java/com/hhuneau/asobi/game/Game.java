package com.hhuneau.asobi.game;

import com.hhuneau.asobi.game.player.Player;
import com.hhuneau.asobi.game.sets.MTGSet;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static javax.persistence.EnumType.STRING;

@Entity
@Data
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
    private Status status = Status.CREATED;
    private Date createdDate = new Date();
    private String authToken;

    @OneToMany(mappedBy = "game", orphanRemoval = true)
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
}

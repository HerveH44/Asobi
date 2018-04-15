package com.hhuneau.asobi.game;

import com.hhuneau.asobi.game.sets.MTGSet;
import com.hhuneau.asobi.websocket.events.CreateGameEvent;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Game {
    @Id
    @GeneratedValue
    private long gameId;
    private String title;
    private boolean isPrivate;
    private GameMode gameMode;
    private GameType gameType;
    private Status status = Status.CREATED;
    private Date createdDate = new Date();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game")
    private Set<Player> players = new HashSet<>();
    @ManyToMany
    @JoinColumn(name = "sets_list")
    private List<MTGSet> sets;

    public static Game of(CreateGameEvent event) {
        final Game game = new Game();
        game.setTitle(event.title);
        game.setPrivate(event.isPrivate);
        game.setGameMode(event.gameMode);
        game.setGameType(event.gameType);
        return game;
    }
}

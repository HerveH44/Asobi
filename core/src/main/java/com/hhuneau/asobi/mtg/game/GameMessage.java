package com.hhuneau.asobi.mtg.game;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class GameMessage {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "game_id")
    private Game game;

    private String name;

    private Date time;

    private String message;

    public static GameMessage of(Game game, String name, Date time, String message) {
        final GameMessage gameMessage = new GameMessage();
        gameMessage.setName(name);
        gameMessage.setGame(game);
        gameMessage.setTime(time);
        gameMessage.setMessage(message);
        return gameMessage;
    }
}

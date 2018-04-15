package com.hhuneau.asobi.game;

import lombok.NoArgsConstructor;

import javax.persistence.*;

/*TODO: See if Player should be an abstract class and HumanPlayer,
*    Bot would be implementations or just Player and Bot as an subclass
*/
@Entity
@NoArgsConstructor
public class Player {
    @Id
    @GeneratedValue
    private long playerId;

    private String name;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    public Player(String name, Game game) {
        this.name = name;
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPlayerId() {
        return playerId;
    }
}

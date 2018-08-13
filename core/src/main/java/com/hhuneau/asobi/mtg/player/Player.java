package com.hhuneau.asobi.mtg.player;

import com.hhuneau.asobi.mtg.pool.Booster;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Player {
    @Id
    @GeneratedValue
    private long playerId;
    private String userId;
    private String name;
    private boolean isBot;
    private int seat;

    @OneToMany(mappedBy = "player")
    private List<Booster> pool;

    @OneToOne(cascade = ALL, optional = false)
    @JoinColumn(name = "player_state_id", unique = true, nullable = false)
    private PlayerState playerState;

    static public Player of(String userId, String name, boolean isBot) {
        final Player player = new Player();
        player.setUserId(userId);
        player.setName(name);
        player.setBot(isBot);
        player.setPlayerState(new PlayerState());
        return player;
    }
}

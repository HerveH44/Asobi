package com.hhuneau.asobi.game;

import com.hhuneau.asobi.game.pool.Booster;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/*TODO: See if Player should be an abstract class and HumanPlayer,
 *    Bot would be implementations or just Player and Bot as an subclass
 */
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

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "player")
    private List<Booster> pool;

    static public Player of(String userId, String name, Game game) {
        final Player player = new Player();
        player.setGame(game);
        player.setUserId(userId);
        player.setName(name);
        return player;
    }
}

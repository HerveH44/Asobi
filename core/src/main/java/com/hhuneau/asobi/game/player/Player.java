package com.hhuneau.asobi.game.player;

import com.hhuneau.asobi.game.pool.Booster;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

/*TODO: See if PlayerEvent should be an abstract class and HumanPlayer,
 *    Bot would be implementations or just PlayerEvent and Bot as an subclass
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

//    @ManyToOne
//    @JoinColumn(name = "game_id")
//    private Game game;

    @OneToMany(mappedBy = "player", orphanRemoval = true)
    private List<Booster> pool;

    @OneToMany
    private List<Booster> remainingPacks;

    static public Player of(String userId, String name) {
        final Player player = new Player();
        player.setUserId(userId);
        player.setName(name);
        return player;
    }
}

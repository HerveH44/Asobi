package com.hhuneau.asobi.mtg.player;

import com.hhuneau.asobi.mtg.pool.Booster;
import com.hhuneau.asobi.mtg.pool.Pack;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

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

    @OneToMany(mappedBy = "player")
    private List<Booster> pool;

    @OneToMany(cascade = ALL)
    @JoinColumn(name = "player_id")
    private List<Pack> remainingPacks;

    static public Player of(String userId, String name) {
        final Player player = new Player();
        player.setUserId(userId);
        player.setName(name);
        return player;
    }
}

package com.hhuneau.asobi.game.pool;

import com.hhuneau.asobi.game.Player;
import com.hhuneau.asobi.game.sets.MTGCard;
import com.hhuneau.asobi.game.sets.MTGSet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class Pool {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "set_id")
    private MTGSet set;

    @ManyToMany
    @JoinColumn(name = "cards_ids")
    private List<MTGCard> cards;

    public static Pool of(Player player, MTGSet set, List<MTGCard> cardPool) {
        final Pool pool = new Pool();
        pool.setCards(cardPool);
        pool.setPlayer(player);
        pool.setSet(set);
        return pool;
    }
}

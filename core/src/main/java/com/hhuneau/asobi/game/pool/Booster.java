package com.hhuneau.asobi.game.pool;

import com.hhuneau.asobi.game.player.Player;
import com.hhuneau.asobi.game.sets.MTGCard;
import com.hhuneau.asobi.game.sets.MTGSet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@ToString(exclude = {"cards", "set", "player"})
public class Booster {
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

    public static Booster of(Player player, MTGSet set, List<MTGCard> cardPool) {
        final Booster booster = new Booster();
        booster.setCards(cardPool);
        booster.setPlayer(player);
        booster.setSet(set);
        return booster;
    }
}

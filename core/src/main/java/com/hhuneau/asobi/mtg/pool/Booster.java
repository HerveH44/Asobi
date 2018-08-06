package com.hhuneau.asobi.mtg.pool;

import com.hhuneau.asobi.mtg.player.Player;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
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

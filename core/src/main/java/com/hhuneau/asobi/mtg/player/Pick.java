package com.hhuneau.asobi.mtg.player;

import com.hhuneau.asobi.ListToStringConverter;
import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.pool.Pack;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class Pick {

    @Id
    @GeneratedValue
    private long id;

    private int round;
    private int pick;

    private String cardPicked;

    @Basic(fetch = FetchType.LAZY)
    @Convert(converter = ListToStringConverter.class)
    @Column(columnDefinition = "text")
    private List<String> cardsPassed;

    public static Pick of(Game game, MTGCard pickedCard, Pack pack) {
        final Pick pick = new Pick();
        pick.cardPicked = pickedCard.getName();
        pick.cardsPassed = pack.getCards().stream().map(MTGCard::getName).collect(Collectors.toList());
        pick.round = game.getRound();
        pick.pick = pack.getPickNumber();
        return pick;
    }
}

package com.hhuneau.asobi.mtg.pool;

import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Pack {

    @Id
    @GeneratedValue
    private long id;

    @ManyToMany
    @JoinColumn(name = "cards_ids")
    private List<MTGCard> cards;

}

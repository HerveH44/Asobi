package com.hhuneau.asobi.mtg.sets;

import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@Setter
public class MTGSet {
    @Id
    private String code;
    private String name;
    @Temporal(TemporalType.DATE)
    private Date releaseDate;
    private String border;
    private String type;

    @OneToMany(cascade = ALL)
    @JoinColumn(name = "set_id")
    private Set<MTGCard> cards;

}

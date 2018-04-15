package com.hhuneau.asobi.game.sets;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Data
public class MTGSet {
    @Id
    private String code;
    private String name;
    @Temporal(TemporalType.DATE)
    private Date releaseDate;
    private String border;
    private String type;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<MTGCard> cards;

}

package com.hhuneau.asobi.mtg.player;

import com.hhuneau.asobi.mtg.pool.Pack;
import com.hhuneau.asobi.mtg.sets.MTGCard;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@Setter
public class PlayerState {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne(mappedBy = "playerState", optional = false)
    private Player player;

    @OneToMany(cascade = ALL)
    @JoinColumn(name = "state_id")
    private List<Pack> waitingPacks;

    @ManyToMany(cascade = ALL)
    @JoinColumn(name = "state_id")
    private List<MTGCard> pickedCards;

    public Pack getWaitingPack() {
        return getWaitingPacks().isEmpty()
            ? null
            : getWaitingPacks().get(0);
    }
}

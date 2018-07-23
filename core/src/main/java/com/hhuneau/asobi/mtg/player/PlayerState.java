package com.hhuneau.asobi.mtg.player;

import com.hhuneau.asobi.mtg.pool.Pack;
import com.hhuneau.asobi.mtg.sets.MTGCard;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
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
    private List<Pack> waitingPacks = new ArrayList<>();

    @ManyToMany(cascade = ALL)
    @JoinColumn(name = "state_id")
    private List<MTGCard> pickedCards = new ArrayList<>();

    private int timeLeft;
    private int pick;
    private int pack;
    private String autoPickId;

    public Pack getWaitingPack() {
        return waitingPacks == null || waitingPacks.isEmpty()
                   ? null
                   : getWaitingPacks().get(0);
    }
}

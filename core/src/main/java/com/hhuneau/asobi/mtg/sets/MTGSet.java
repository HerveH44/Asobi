package com.hhuneau.asobi.mtg.sets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hhuneau.asobi.mtg.sets.booster.ListToSlotConverter;
import com.hhuneau.asobi.mtg.sets.booster.Slot;
import com.hhuneau.asobi.mtg.sets.booster.SlotDeserializer;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MTGSet {
    @Id
    private String code;
    private String name;
    @Temporal(TemporalType.DATE)
    private Date releaseDate;
    private String type;
    @Basic(fetch = FetchType.LAZY)
    @Convert(converter = ListToSlotConverter.class)
    @Column(columnDefinition = "text")
    @JsonSetter("booster")
    @JsonDeserialize(using = SlotDeserializer.class)
    private List<Slot> slotList;
    @OneToMany(cascade = ALL)
    @JoinColumn(name = "set_id")
    private Set<MTGCard> cards;
}

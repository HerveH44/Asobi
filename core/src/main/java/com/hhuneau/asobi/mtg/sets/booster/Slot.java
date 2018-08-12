package com.hhuneau.asobi.mtg.sets.booster;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@Entity
@Getter
@Setter
@JsonDeserialize(using = SlotDeserializer.class)
public class Slot {

    @Id
    @GeneratedValue
    private long id;

    @ElementCollection
    @Enumerated(STRING)
    private List<SlotType> values;

}

package com.hhuneau.asobi.mtg.sets.booster;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hhuneau.asobi.ListToStringConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@JsonDeserialize(using = SlotDeserializer.class)
public class Slot {

    @Id
    @GeneratedValue
    private long id;

    @Basic(fetch = FetchType.LAZY)
    @Convert(converter = ListToStringConverter.class)
    @Column(columnDefinition = "text")
    private List<SlotType> values;

}

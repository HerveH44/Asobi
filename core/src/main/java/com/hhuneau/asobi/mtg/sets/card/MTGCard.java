package com.hhuneau.asobi.mtg.sets.card;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hhuneau.asobi.ListToStringConverter;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(indexes = {@Index(name = "card_name_index", columnList = "name")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class MTGCard {
    @Id
    private String id;
    private long multiverseid;
    private String name;
    private int cmc;
    private String manaCost;
    @Basic(fetch = FetchType.LAZY)
    @Convert(converter = ListToStringConverter.class)
    @Column(columnDefinition = "text")
    private List<String> colors;
    private String color;
    private String layout;
    @Basic(fetch = FetchType.LAZY)
    @Convert(converter = ListToStringConverter.class)
    @Column(columnDefinition = "text")
    private List<String> names;
    private String number;
    private String power;
    @Enumerated(EnumType.STRING)
    private Rarity rarity;
    @Basic(fetch = FetchType.LAZY)
    @Convert(converter = ListToStringConverter.class)
    @Column(columnDefinition = "text")
    private List<String> subtypes;
    private String type;
    @Basic(fetch = FetchType.LAZY)
    @Convert(converter = ListToStringConverter.class)
    @Column(columnDefinition = "text")
    private List<String> types;
    private boolean isDoubleFace;
    private long flipMultiverseid;
    private boolean timeshifted;
    @Basic(fetch = FetchType.LAZY)
    @Convert(converter = ListToStringConverter.class)
    @Column(columnDefinition = "text")
    private List<String> supertypes;

    @ManyToOne
    @JoinColumn(name = "set_id")
    @JsonIgnore
    private MTGSet set;
}

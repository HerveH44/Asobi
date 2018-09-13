package com.hhuneau.asobi.mtg.sets.card;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class MTGCard {
    @Id
    private String id;
    private String artist;
    private int cmc;
    @Basic(fetch = FetchType.LAZY)
    @Convert(converter = ListToStringConverter.class)
    @Column(columnDefinition = "text")
    private List<String> colorIdentity;
    @Basic(fetch = FetchType.LAZY)
    @Convert(converter = ListToStringConverter.class)
    @Column(columnDefinition = "text")
    private List<String> colors;
    private String color;
    private String imageName;
    private String layout;
    private String manaCost;
    private long multiverseid;
    private String name;
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
    private String toughness;
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

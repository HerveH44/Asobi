package com.hhuneau.asobi.game.sets;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class MTGCard {
    @Id
    private String id;
    private String artist;
    private int cmc;
    @ElementCollection
    private List<String> colorIdentity;
    @ElementCollection
    private List<String> colors;
    private String imageName;
    private String layout;
    private String manaCost;
    private long multiverseid;
    private String name;
    private String number;
    private String power;
    @Enumerated(EnumType.STRING)
    private Rarity rarity;
    @ElementCollection
    private List<String> subtypes;
    private String toughness;
    private String type;
    @ElementCollection
    private List<String> types;
}

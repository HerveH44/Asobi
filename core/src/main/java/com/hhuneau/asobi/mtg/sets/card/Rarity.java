package com.hhuneau.asobi.mtg.sets.card;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Rarity {
    @JsonProperty("common")
    COMMON,
    @JsonProperty("uncommon")
    UNCOMMON,
    @JsonProperty("rare")
    RARE,
    @JsonProperty("mythic")
    MYTHIC_RARE,
    @JsonProperty("basic")
    BASIC_LAND
}

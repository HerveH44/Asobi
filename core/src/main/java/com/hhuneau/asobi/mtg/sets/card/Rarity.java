package com.hhuneau.asobi.mtg.sets.card;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Rarity {
    @JsonProperty("Common")
    COMMON,
    @JsonProperty("Uncommon")
    UNCOMMON,
    @JsonProperty("Rare")
    RARE,
    @JsonProperty("Mythic Rare")
    MYTHIC_RARE,
    @JsonProperty("Basic Land")
    BASIC_LAND,
    @JsonProperty("Special")
    SPECIAL
}

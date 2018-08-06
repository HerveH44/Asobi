package com.hhuneau.asobi.mtg.sets.cardmodifier;

import com.hhuneau.asobi.mtg.sets.card.MTGCard;

import java.util.Set;

public interface MTGCardModifier {

    void modify(MTGCard card, Set<MTGCard> cards);
}

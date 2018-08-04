package com.hhuneau.asobi.mtg.sets;

import java.util.Set;

public interface MTGCardModifier {

    void modify(MTGCard card, Set<MTGCard> cards);
}

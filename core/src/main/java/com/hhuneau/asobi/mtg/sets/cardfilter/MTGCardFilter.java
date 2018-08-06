package com.hhuneau.asobi.mtg.sets.cardfilter;

import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import com.hhuneau.asobi.mtg.sets.MTGSet;

import java.util.Set;

public interface MTGCardFilter {

    boolean isInterested(MTGSet set);

    Set<MTGCard> apply(MTGSet set, Set<MTGCard> cards);
}

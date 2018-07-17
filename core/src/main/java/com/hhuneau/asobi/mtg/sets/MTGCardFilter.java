package com.hhuneau.asobi.mtg.sets;

import java.util.Set;

public interface MTGCardFilter {

    boolean isInterested(MTGSet set);

    Set<MTGCard> apply(Set<MTGCard> cards);
}

package com.hhuneau.asobi.mtg.pool.set;

import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;

import java.util.List;

public interface SetBoosterMaker {

    boolean isInterestedIn(String setCode);

    List<MTGCard> make(MTGSet set);
}

package com.hhuneau.asobi.mtg.sets.card;

import java.util.List;

public interface MTGCardService {

    List<MTGCard> getCards(List<String> cardNames);
}

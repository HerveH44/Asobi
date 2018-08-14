package com.hhuneau.asobi.mtg.sets.card;

import java.util.Optional;

public interface MTGCardService {

    Optional<MTGCard> getCard(String cardName);
}

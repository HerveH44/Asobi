package com.hhuneau.asobi.mtg.sets;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DoubleFaceCardFilter implements MTGCardFilter {
    @Override
    public boolean isInterested(MTGSet set) {
        return true;
    }

    @Override
    public Set<MTGCard> apply(Set<MTGCard> cards) {
        return cards.stream()
            .filter(this::filter)
            .collect(Collectors.toSet());
    }

    private boolean filter(MTGCard card) {
        final boolean isNotDoubleFaced = !card.getLayout().contains("double-faced");
        final boolean isNotFlippedSide = card.getNames() == null || card.getNames().isEmpty() || card.getName().equals(card.getNames().get(0));
        return isNotDoubleFaced || isNotFlippedSide;
    }
}

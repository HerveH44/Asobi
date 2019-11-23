package com.hhuneau.asobi.mtg.sets.cardfilter;

import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TokenFilter implements MTGCardFilter {
    @Override
    public boolean isInterested(MTGSet set) {
        return true;
    }

    @Override
    public Set<MTGCard> apply(MTGSet set, Set<MTGCard> cards) {
        return cards.stream()
            .filter(card -> card.getLayout() != null && !card.getLayout().equals("token"))
            .collect(Collectors.toSet());
    }
}

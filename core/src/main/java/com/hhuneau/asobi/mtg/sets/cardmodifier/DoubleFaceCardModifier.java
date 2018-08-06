package com.hhuneau.asobi.mtg.sets.cardmodifier;

import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class DoubleFaceCardModifier implements MTGCardModifier {

    @Override
    public void modify(MTGCard card, Set<MTGCard> cards) {
        final List<String> names = card.getNames();

        if (names != null && names.size() > 1) {
            card.setDoubleFace(true);
            cards.stream().filter(cd -> cd.getName().equals(names.get(names.size() - 1)))
                .findFirst()
                .ifPresent(cd -> card.setFlipMultiverseid(cd.getMultiverseid()));
        }
    }
}

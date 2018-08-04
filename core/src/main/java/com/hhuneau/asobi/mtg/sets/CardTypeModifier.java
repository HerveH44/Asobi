package com.hhuneau.asobi.mtg.sets;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class CardTypeModifier implements MTGCardModifier {
    @Override
    public void modify(MTGCard card, Set<MTGCard> cards) {
        final List<String> types = card.getTypes();
        card.setType(types.get(types.size() - 1));
    }
}

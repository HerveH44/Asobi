package com.hhuneau.asobi.mtg.sets.cardmodifier;

import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MTGCardNameModifier implements MTGCardModifier {

    @Override
    public void modify(MTGCard card, Set<MTGCard> cards) {
        if(card.getLayout().matches("^aftermath$|^split$")) {
            card.setName(String.join(" // ", card.getNames()));
        }
    }
}

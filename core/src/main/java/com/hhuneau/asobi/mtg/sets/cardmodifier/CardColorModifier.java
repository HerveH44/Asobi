package com.hhuneau.asobi.mtg.sets.cardmodifier;

import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CardColorModifier implements MTGCardModifier {
    @Override
    public void modify(MTGCard card, Set<MTGCard> cards) {

        // In the case of lands, they actually have no color field ...
        final String color = (card.getColors() == null || card.getColors().isEmpty()) ?
                "Colorless" :
                card.getColors().size() > 1 ? "Multicolor" : card.getColors().get(0);

        card.setColor(color);
    }
}

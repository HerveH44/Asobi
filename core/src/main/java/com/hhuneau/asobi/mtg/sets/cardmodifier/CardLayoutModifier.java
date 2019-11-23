package com.hhuneau.asobi.mtg.sets.cardmodifier;

import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CardLayoutModifier implements MTGCardModifier {
    @Override
    public void modify(MTGCard card, Set<MTGCard> cards) {
        if(card.getLayout() == null) {
            card.setLayout("normal");
        }
    }
}

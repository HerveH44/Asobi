package com.hhuneau.asobi.mtg.pool.set;

import com.hhuneau.asobi.mtg.sets.CardsGroupedByRarity;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import com.hhuneau.asobi.mtg.sets.card.Rarity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.hhuneau.asobi.mtg.sets.card.Rarity.*;

public class DOMBoosterMaker extends DefaultBoosterMaker {
    @Override
    public boolean isInterestedIn(String setCode) {
        return setCode.equals("DOM");
    }

    @Override
    List<MTGCard> postProcessCardList(MTGSet set, List<MTGCard> cardList) {

        // Booster (cardList) must contain at least one legendary creature
        // in the uncommon or rare slot
        final List<Rarity> rarityList = List.of(UNCOMMON, RARE, MYTHIC_RARE);
        if (cardList.stream()
            .anyMatch(card ->
                rarityList.contains(card.getRarity()) &&
                    isLegendaryCreature(card))) {
            return cardList;
        }

        Collections.shuffle(cardList);
        final CardsGroupedByRarity cardsGroupedByRarity = CardsGroupedByRarity.of(set);
        for (int i = 0; i < cardList.size(); i++) {
            final MTGCard card = cardList.get(i);
            if (rarityList.contains(card.getRarity())) {
                final List<MTGCard> cards = cardsGroupedByRarity.get(card.getRarity()).stream()
                    .filter(this::isLegendaryCreature)
                    .collect(Collectors.toList());
                cardList.set(i, choose(cards, 1).get(0));
                return cardList;
            }
        }

        return cardList;
    }

    private boolean isLegendaryCreature(MTGCard card) {
        return card.getType().equals("Creature")
            && card.getSupertypes() != null
            && card.getSupertypes().contains("Legendary");
    }
}

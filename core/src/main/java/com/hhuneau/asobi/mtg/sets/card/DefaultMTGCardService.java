package com.hhuneau.asobi.mtg.sets.card;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DefaultMTGCardService implements MTGCardService {

    private final MTGCardRepository cardRepository;

    public DefaultMTGCardService(MTGCardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public List<MTGCard> getCards(List<String> cardNames) {
        final List<MTGCard> cards = cardRepository.findAll();
        final List<MTGCard> filteredList = new ArrayList<>();
        cards.stream()
            .filter(card-> !card.getSet().getType().equals("masterpiece"))
            .filter(card -> cardNames.contains(card.getName().toLowerCase()))
            .forEach(card -> {
                if (filteredList.stream().noneMatch(savedCard -> savedCard.getName().equals(card.getName()))) {
                    filteredList.add(card);
                }
            });

        return filteredList;
    }
}

package com.hhuneau.asobi.mtg.sets.card;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class DefaultMTGCardService implements MTGCardService {

    private final MTGCardRepository cardRepository;

    public DefaultMTGCardService(MTGCardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public Optional<MTGCard> getCard(String cardName) {
        return cardRepository.findTop2ByNameLikeOrderByMultiverseidAsc(cardName)
            .filter(card -> card.getMultiverseid() != 0)
            .findFirst();
    }
}

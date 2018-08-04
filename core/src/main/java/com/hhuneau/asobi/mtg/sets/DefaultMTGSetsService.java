package com.hhuneau.asobi.mtg.sets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class DefaultMTGSetsService implements MTGSetsService {
    private final MTGSetRepository setRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultMTGSetsService.class);
    private final List<MTGCardFilter> filterList;
    private final List<MTGCardModifier> modifierList;

    public DefaultMTGSetsService(MTGSetRepository setRepository, List<MTGCardFilter> filterList, List<MTGCardModifier> cardModifierList) {
        this.setRepository = setRepository;
        this.filterList = filterList;
        this.modifierList = cardModifierList;
    }

    public void saveSet(MTGSet set) {
        try {

            final Set<MTGCard> cards = filter(set, set.getCards());
            set.setCards(cards);
            setRepository.save(set);
        } catch (final Exception e) {
            LOGGER.info("can't save set {} because {}", set.getCode(), e.getMessage());
        }
    }

    private Set<MTGCard> filter(MTGSet set, final Set<MTGCard> cards) {
        final List<MTGCardFilter> filterList = this.filterList.stream()
            .filter(filter -> filter.isInterested(set))
            .collect(Collectors.toList());
        Set<MTGCard> remainingCards = cards;
        for (MTGCardFilter filter : filterList) {
            remainingCards = filter.apply(set, cards);
        }

        /* Modify the remaining cards
            For example: add colors...
            To modify a card, use a component that implements MTGCardModifier
         */
        for(final MTGCard card : remainingCards) {
            this.modifierList.forEach(modifier -> modifier.modify(card, cards));
        }

        return remainingCards;
    }

    public List<MTGSet> getSets() {
        return setRepository.findAll();
    }

    public Optional<MTGSet> getSet(String setCode) {
        return setRepository.findById(setCode);
    }

    public List<MTGSet> getSets(List<String> sets) {
        return sets.stream()
            .map(this::getSet)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }
}

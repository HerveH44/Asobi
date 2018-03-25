package com.hhuneau.asobi.sets;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class MTGSetsService {
    private final MTGCardRepository cardRepository;
    private final MTGSetRepository setRepository;

    public MTGSetsService(MTGCardRepository cardRepository, MTGSetRepository setRepository) {
        this.cardRepository = cardRepository;
        this.setRepository = setRepository;
    }

    public void saveSet(MTGSet set) {
        setRepository.save(set);
    }

    public void saveCard(MTGCard mtgCard) {
        cardRepository.save(mtgCard);
    }

    public List<MTGSet> getSets() {
        return setRepository.findAll();
    }
}

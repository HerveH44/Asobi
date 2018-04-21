package com.hhuneau.asobi.game.sets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class MTGSetsService {
    private final MTGCardRepository cardRepository;
    private final MTGSetRepository setRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(MTGSetsService.class);

    public MTGSetsService(MTGCardRepository cardRepository, MTGSetRepository setRepository) {
        this.cardRepository = cardRepository;
        this.setRepository = setRepository;
    }

    public void saveSet(MTGSet set) {
        try {
            setRepository.save(set);
        } catch (final Exception e) {
            LOGGER.info("can't save set {} because {}", set.getCode(), e.getMessage());
        }
    }

    public List<MTGSet> getSets() {
        return setRepository.findAll();
    }

    public MTGSet getSet(String setCode) {
        return setRepository.findOne(setCode);
    }
}

package com.hhuneau.asobi.game.sets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DefaultMTGSetsService implements MTGSetsService {
    private final MTGSetRepository setRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultMTGSetsService.class);

    public DefaultMTGSetsService(MTGSetRepository setRepository) {
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

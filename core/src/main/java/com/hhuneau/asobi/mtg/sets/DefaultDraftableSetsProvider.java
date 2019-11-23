package com.hhuneau.asobi.mtg.sets;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Component
public class DefaultDraftableSetsProvider implements DraftableSetsProvider {

    private final MTGSetsService setsService;
    private Map<String, List<SetDTO>> draftableSets;

    public DefaultDraftableSetsProvider(MTGSetsService setsService) {
        this.setsService = setsService;
    }

    @Override
    public Map<String, List<SetDTO>> getDraftableSets() {
        return draftableSets;
    }

    @EventListener
    public void fetchDraftableSets(ContextRefreshedEvent event) {
        draftableSets = setsService.getSets().stream()
            .filter(set -> !List.of("masterpiece", "planechase", "starter", "commander").contains(set.getType()))
            .map(SetDTO::of)
            .collect(groupingBy(SetDTO::getType));
    }
}

package com.hhuneau.asobi.mtg.pool.set.masterpiece;

import com.hhuneau.asobi.mtg.sets.MTGSetsService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OGWBoosterMaker extends MasterPieceBoosterMaker {

    public OGWBoosterMaker(MTGSetsService setsService) {
        super(setsService);
    }

    @Override
    public boolean isInterestedIn(String setCode) {
        return setCode.equals("OGW");
    }

    @Override
    String getMasterPieceSet() {
        return "EXP";
    }

    @Override
    List<String> getMasterPieceList() {
        return List.of("mystic gate", "sunken ruins", "graven cairns", "fire-lit thicket", "wooded bastion", "fetid heath", "cascade bluffs", "twilight mire", "rugged prairie", "flooded grove", "ancient tomb", "dust bowl", "eye of ugin", "forbidden orchard", "horizon canopy", "kor haven", "mana confluence", "strip mine", "tectonic edge", "wasteland");
    }
}

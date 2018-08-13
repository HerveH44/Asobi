package com.hhuneau.asobi.mtg.pool.set.masterpiece;

import com.hhuneau.asobi.mtg.sets.MTGSetsService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BFZBoosterMaker extends MasterPieceBoosterMaker {
    public BFZBoosterMaker(MTGSetsService setsService) {
        super(setsService);
    }

    @Override
    public boolean isInterestedIn(String setCode) {
        return setCode.equals("BFZ");
    }

    @Override
    String getMasterPieceSet() {
        return "EXP";
    }

    @Override
    List<String> getMasterPieceList() {
        return List.of("prairie stream", "sunken hollow", "smoldering marsh", "cinder glade", "canopy vista", "hallowed fountain", "watery grave", "blood crypt", "stomping ground", "temple garden", "godless shrine", "steam vents", "overgrown tomb", "sacred foundry", "breeding pool", "flooded strand", "polluted delta", "bloodstained mire", "wooded foothills", "windswept heath", "marsh flats", "scalding tarn", "verdant catacombs", "arid mesa", "misty rainforest");
    }
}

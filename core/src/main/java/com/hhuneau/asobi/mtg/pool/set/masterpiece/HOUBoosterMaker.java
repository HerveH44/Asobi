package com.hhuneau.asobi.mtg.pool.set.masterpiece;

import com.hhuneau.asobi.mtg.sets.MTGSetsService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HOUBoosterMaker extends MasterPieceBoosterMaker {

    public HOUBoosterMaker(MTGSetsService setsService) {
        super(setsService);
    }

    @Override
    public boolean isInterestedIn(String setCode) {
        return setCode.equals("HOU");
    }

    @Override
    String getMasterPieceSet() {
        return "MPS_AKH";
    }

    @Override
    List<String> getMasterPieceList() {
        return List.of("Armageddon", "Capsize", "Forbid", "Omniscience", "Opposition", "Sunder", "Threads of Disloyalty", "Avatar of Woe", "Damnation", "Desolation Angel", "Diabolic Edict", "Doomsday", "No Mercy", "Slaughter Pact", "Thoughtseize", "Blood Moon", "Boil", "Shatterstorm", "Through the Breach", "Choke", "The Locust God", "Lord of Extinction", "The Scarab God", "The Scorpion God");
    }
}

package com.hhuneau.asobi.mtg.pool.set.masterpiece;

import com.hhuneau.asobi.mtg.sets.MTGSetsService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AERBoosterMaker extends MasterPieceBoosterMaker {

    public AERBoosterMaker(MTGSetsService setsService) {
        super(setsService);
    }

    @Override
    public boolean isInterestedIn(String setCode) {
        return setCode.equals("AER");
    }

    @Override
    String getMasterPieceSet() {
        return "MPS";
    }

    @Override
    List<String> getMasterPieceList() {
        return List.of("Paradox Engine", "Planar Bridge", "Arcbound Ravager", "Black Vise", "Chalice of the Void", "Defense Grid", "Duplicant", "Engineered Explosives", "Ensnaring Bridge", "Extraplanar Lens", "Grindstone", "Meekstone", "Oblivion Stone", "Ornithopter", "Sphere of Resistance", "Staff of Domination", "Sundering Titan", "Sword of Body and Mind", "Sword of War and Peace", "Trinisphere", "Vedalken Shackles", "Wurmcoil Engine");
    }
}

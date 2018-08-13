package com.hhuneau.asobi.mtg.pool.set.masterpiece;

import com.hhuneau.asobi.mtg.sets.MTGSetsService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KLDBoosterMaker extends MasterPieceBoosterMaker {
    public KLDBoosterMaker(MTGSetsService setsService) {
        super(setsService);
    }

    @Override
    public boolean isInterestedIn(String setCode) {
        return setCode.equals("KLD");
    }

    @Override
    String getMasterPieceSet() {
        return "MPS";
    }

    @Override
    List<String> getMasterPieceList() {
        return List.of("cataclysmic gearhulk", "torrential gearhulk", "noxious gearhulk", "combustible gearhulk", "verdurous gearhulk", "aether vial", "champion's helm", "chromatic lantern", "chrome mox", "cloudstone curio", "crucible of worlds", "gauntlet of power", "hangarback walker", "lightning greaves", "lotus petal", "mana crypt", "mana vault", "mind's eye", "mox opal", "painter's servant", "rings of brighthearth", "scroll rack", "sculpting steel", "sol ring", "solemn simulacrum", "static orb", "steel overseer", "sword of feast and famine", "sword of fire and ice", "sword of light and shadow");
    }
}

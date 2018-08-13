package com.hhuneau.asobi.mtg.pool.set;

import com.hhuneau.asobi.mtg.sets.MTGSetsService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AKHBoosterMaker extends MasterPieceBoosterMaker {

    public AKHBoosterMaker(MTGSetsService setsService) {
        super(setsService);
    }

    @Override
    public boolean isInterestedIn(String setCode) {
        return setCode.equals("AKH");
    }

    @Override
    String getMasterPieceSet() {
        return "MPS_AKH";
    }

    @Override
    List<String> getMasterPieceList() {
        return List.of("Austere Command", "Aven Mindcensor", "Containment Priest", "Loyal Retainers", "Worship", "Wrath of God", "Consecrated Sphinx", "Counterbalance", "Counterspell", "Cryptic Command", "Daze", "Divert", "Force of Will", "Pact of Negation", "Spell Pierce", "Stifle", "Attrition", "Dark Ritual", "Diabolic Intent", "Entomb", "Mind Twist", "Aggravated Assault", "Chain Lightning", "Maelstrom Pulse", "Vindicate", "Hazoret the Fervent", "Kefnet the Mindful", "Oketra the True", "Bontu the Glorified", "Rhonas the Indomitable");
    }
}

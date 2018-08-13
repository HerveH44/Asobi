package com.hhuneau.asobi.mtg.pool.set.masterpiece;

import com.hhuneau.asobi.mtg.pool.set.DefaultBoosterMaker;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.MTGSetsService;
import com.hhuneau.asobi.mtg.sets.booster.Slot;
import com.hhuneau.asobi.mtg.sets.booster.SlotType;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.hhuneau.asobi.mtg.sets.booster.SlotType.COMMON;
import static com.hhuneau.asobi.mtg.sets.booster.SlotType.MASTERPIECE;

public abstract class MasterPieceBoosterMaker extends DefaultBoosterMaker {

    private final MTGSetsService setsService;
    private MTGSet masterPieceSet;

    public MasterPieceBoosterMaker(MTGSetsService setsService) {
        this.setsService = setsService;
    }

    @Override
    protected List<Slot> additionalProcessSlotValues(MTGSet set, List<Slot> filteredList) {
        if (masterPieceSet == null) {
            masterPieceSet = setsService.getSet(getMasterPieceSet()).orElseThrow(() -> new IllegalStateException("masterpiece set not found"));
        }

        final int randomNumber = new Random().nextInt(144);
        if (randomNumber == 0) {
            for (int i = 0; i < filteredList.size(); i++) {
                final Slot slot = filteredList.get(i);
                if (isCommonSlot(slot)) {
                    slot.setValues(List.of(MASTERPIECE));
                    filteredList.set(i, slot);
                    return filteredList;
                }
            }
        }

        return filteredList;
    }

    private boolean isCommonSlot(Slot slot) {
        return slot.getValues().size() == 1 && slot.getValues().contains(COMMON);
    }

    @Override
    protected List<MTGCard> handleUnexpectedSlotValue(MTGSet set, SlotType slotType, int occurrences) {
        if (slotType.equals(MASTERPIECE)) {
            final List<MTGCard> cardList = masterPieceSet.getCards().stream()
                .filter(card -> getMasterPieceList().contains(card.getName().toLowerCase()))
                .collect(Collectors.toList());

            return choose(cardList, occurrences);
        }
        return null;
    }

    abstract String getMasterPieceSet();

    abstract List<String> getMasterPieceList();
}

package com.hhuneau.asobi.mtg.pool.set;

import com.hhuneau.asobi.mtg.sets.CardsGroupedByRarity;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.booster.Slot;
import com.hhuneau.asobi.mtg.sets.booster.SlotType;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import com.hhuneau.asobi.mtg.sets.card.Rarity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.hhuneau.asobi.mtg.sets.booster.SlotType.*;

/*
    This class is used as a template pattern.
    It translates infos from "booster" field of a set into a card.
    Override methods you need and keep "make" intact.
 */
public class DefaultBoosterMaker implements SetBoosterMaker {

    private static final List<SlotType> filteredSlotTypes = List.of(MARKETING, CHECKLIST);

    @Override
    public boolean isInterestedIn(String setCode) {
        return false;
    }

    @Override
    public List<MTGCard> make(MTGSet set) {
        final CardsGroupedByRarity cardsByRarity = CardsGroupedByRarity.of(set);
        final List<SlotDTO> slotList = preProcessSlotValues(set, set.getBooster());
        final List<MTGCard> cardList = slotList.stream()
            .map(slot -> {
                final List<SlotType> slotValues = slot.getValues();
                final int occurrences = slot.getOccurrences();

                if (slotValues.size() == 1) {
                    return handleSlotType(set, cardsByRarity, slotValues.get(0), occurrences);
                } else if (isChoiceBetweenRareAndMythic(slotValues)) {
                    return handleChoiceBetweenRareAndMythic(cardsByRarity, occurrences);
                }
                return handleUnexpectedSlotValues(set, slotValues, occurrences);
            })
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        return postProcessCardList(set, cardList);
    }

    List<MTGCard> handleSlotType(MTGSet set, CardsGroupedByRarity cardsByRarity, SlotType slotType, int occurrences) {
        switch (slotType) {
            case MYTHIC_RARE:
                return handleMythicRare(cardsByRarity, occurrences);
            case RARE:
                return handleRare(cardsByRarity, occurrences);
            case UNCOMMON:
                return handleUncommon(cardsByRarity, occurrences);
            case COMMON:
                return handleCommon(cardsByRarity, occurrences);
            case DOUBLE_FACED:
                return handleDoubleFaced(set, occurrences);
            case DOUBLE_FACED_COMMON:
                return handleDoubleFacedCommon(set, occurrences);
            case DOUBLE_FACED_MYTHIC_RARE:
                return handleDoubleFacedMythic(set, occurrences);
            case DOUBLE_FACED_RARE:
                return handleDoubleFacedRare(set, occurrences);
            case DOUBLE_FACED_UNCOMMON:
                return handleDoubleFacedUncommon(set, occurrences);
            case TIMESHIFTED_PURPLE:
                return handleTimeShiftedPurple(set, occurrences);
            case TIMESHIFTED_COMMON:
                return handleTimeShiftedCommon(set, occurrences);
            case TIMESHIFTED_RARE:
                return handleTimeShiftedRare(set, occurrences);
            case TIMESHIFTED_UNCOMMON:
                return handleTimeShiftedUncommon(set, occurrences);
            case FOIL:
                return handleFoil(set, occurrences);
            default:
                return handleUnexpectedSlotValue(set, slotType, occurrences);
        }
    }

    List<MTGCard> handleFoil(MTGSet set, int occurrences) {
        final int rngFoil = new Random().nextInt(6);
        final CardsGroupedByRarity cardsByRarity = CardsGroupedByRarity.of(set);
        if (rngFoil == 0) {
            if (cardsByRarity.get(Rarity.MYTHIC_RARE).isEmpty()) {
                return choose(cardsByRarity.get(Rarity.RARE), occurrences);
            } else {
                final int rarityChance = cardsByRarity.get(Rarity.MYTHIC_RARE).size() + cardsByRarity.get(Rarity.RARE).size() * 2;
                if (new Random().nextInt(rarityChance) < cardsByRarity.get(Rarity.MYTHIC_RARE).size()) {
                    return choose(cardsByRarity.get(Rarity.MYTHIC_RARE), occurrences);
                }
                return choose(cardsByRarity.get(Rarity.RARE), occurrences);
            }
        } else if (rngFoil < 4) {
            return choose(cardsByRarity.get(Rarity.UNCOMMON), occurrences);
        }
        return choose(cardsByRarity.get(Rarity.COMMON), occurrences);
    }

    List<SlotDTO> preProcessSlotValues(MTGSet set, List<Slot> values) {
        final List<Slot> filteredList = values.stream()
            .map(slot -> {
                slot.getValues().removeIf(filteredSlotTypes::contains);
                return slot.getValues().isEmpty() ? null : slot;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        try {
            // Foils were introduced as common-slot at timeSpiral
            final String timeSpiralReleaseDate = "2006-10-05";
            final Date timeSpiralDate = new SimpleDateFormat("yyyy-MM-dd").parse(timeSpiralReleaseDate);
            if (set.getReleaseDate().after(timeSpiralDate) && isFoil()) {

                boolean foilSet = false;
                for (int i = 0; i < filteredList.size(); i++) {
                    final Slot slot = filteredList.get(i);
                    if (!foilSet && slot.getValues().size() == 1 && slot.getValues().contains(COMMON)) {
                        final Slot foilSlot = new Slot();
                        foilSlot.setValues(List.of(FOIL));
                        foilSet = true;
                        filteredList.set(i, foilSlot);
                    }
                }
            }
        } catch (ParseException e) {
        }

        final List<SlotDTO> list = new ArrayList<>();
        filteredList.forEach(slot ->
            list.stream()
                .filter(slotDTO -> slotDTO.getValues().containsAll(slot.getValues()))
                .findFirst()
                .ifPresentOrElse(
                    slotDTO -> slotDTO.occurrences = slotDTO.occurrences + 1,
                    () -> list.add(SlotDTO.of(slot))));
        return list;
    }

    private boolean isFoil() {
        return new Random().nextInt(7) == 0;
    }

    List<MTGCard> postProcessCardList(MTGSet set, List<MTGCard> cardList) {
            return cardList;
    }

    List<MTGCard> handleUnexpectedSlotValues(MTGSet set, List<SlotType> slotValues, int occurrences) {
        return null;
    }

    List<MTGCard> handleChoiceBetweenRareAndMythic(CardsGroupedByRarity cardsByRarity, int occurrences) {
        final Rarity rarity = isMythicCard() ? Rarity.MYTHIC_RARE : Rarity.RARE;
        return choose(cardsByRarity.get(rarity), occurrences);
    }

    List<MTGCard> handleUnexpectedSlotValue(MTGSet set, SlotType slotType, int occurrences) {
        return null;
    }

    List<MTGCard> handleTimeShiftedUncommon(MTGSet set, int occurrences) {
        return choose(set.getCards().stream().filter(card -> card.isTimeshifted() && card.getRarity().equals(Rarity.UNCOMMON)).collect(Collectors.toList()), occurrences);
    }

    List<MTGCard> handleTimeShiftedRare(MTGSet set, int occurrences) {
        return choose(set.getCards().stream().filter(card -> card.isTimeshifted() && card.getRarity().equals(Rarity.RARE)).collect(Collectors.toList()), occurrences);
    }

    List<MTGCard> handleTimeShiftedCommon(MTGSet set, int occurrences) {
        return choose(set.getCards().stream().filter(card -> card.isTimeshifted() && card.getRarity().equals(Rarity.COMMON)).collect(Collectors.toList()), occurrences);
    }

    List<MTGCard> handleTimeShiftedPurple(MTGSet set, int occurrences) {
        return choose(set.getCards().stream().filter(MTGCard::isTimeshifted).collect(Collectors.toList()), occurrences);
    }

    List<MTGCard> handleDoubleFacedUncommon(MTGSet set, int occurrences) {
        return choose(set.getCards().stream().filter(card -> card.getLayout().equals("double-faced") && card.getRarity().equals(Rarity.UNCOMMON)).collect(Collectors.toList()), occurrences);
    }

    List<MTGCard> handleDoubleFacedRare(MTGSet set, int occurrences) {
        return choose(set.getCards().stream().filter(card -> card.getLayout().equals("double-faced") && card.getRarity().equals(Rarity.RARE)).collect(Collectors.toList()), occurrences);
    }

    List<MTGCard> handleDoubleFacedMythic(MTGSet set, int occurrences) {
        return choose(set.getCards().stream().filter(card -> card.getLayout().equals("double-faced") && card.getRarity().equals(Rarity.MYTHIC_RARE)).collect(Collectors.toList()), occurrences);
    }

    List<MTGCard> handleDoubleFacedCommon(MTGSet set, int occurrences) {
        return choose(set.getCards().stream().filter(card -> card.getLayout().equals("double-faced") && card.getRarity().equals(Rarity.COMMON)).collect(Collectors.toList()), occurrences);
    }

    List<MTGCard> handleDoubleFaced(MTGSet set, int occurrences) {
        return choose(set.getCards().stream().filter(card -> card.getLayout().equals("double-faced")).collect(Collectors.toList()), occurrences);
    }

    List<MTGCard> handleCommon(CardsGroupedByRarity cardsByRarity, int occurrences) {
        return choose(cardsByRarity.get(Rarity.COMMON), occurrences);
    }

    List<MTGCard> handleUncommon(CardsGroupedByRarity cardsByRarity, int occurrences) {
        return choose(cardsByRarity.get(Rarity.UNCOMMON), occurrences);
    }

    List<MTGCard> handleRare(CardsGroupedByRarity cardsByRarity, int occurrences) {
        return choose(cardsByRarity.get(Rarity.RARE), occurrences);
    }

    List<MTGCard> handleMythicRare(CardsGroupedByRarity cardsByRarity, int occurrences) {
        return choose(cardsByRarity.get(Rarity.MYTHIC_RARE), occurrences);
    }

    boolean isChoiceBetweenRareAndMythic(List<SlotType> slotValues) {
        return slotValues.size() == 2 && slotValues.contains(MYTHIC_RARE) && slotValues.contains(RARE);
    }

    boolean isMythicCard() {
        return new Random().nextInt(8) == 0;
    }

    List<MTGCard> choose(List<MTGCard> list, int occurrences) {
        Collections.shuffle(list);
        return list.subList(0, occurrences);
    }
}

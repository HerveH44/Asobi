package com.hhuneau.asobi.mtg.pool;

import com.hhuneau.asobi.mtg.game.Game;
import com.hhuneau.asobi.mtg.game.GameType;
import com.hhuneau.asobi.mtg.player.Player;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import com.hhuneau.asobi.mtg.sets.MTGSet;
import com.hhuneau.asobi.mtg.sets.card.Rarity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hhuneau.asobi.mtg.game.GameMode.NORMAL;
import static com.hhuneau.asobi.mtg.sets.card.Rarity.*;

public class BoosterMakerTest {

    private NormalPoolMaker normalPoolMaker;
    private Game game;


    @Before
    public void setUp() {
        normalPoolMaker = new NormalPoolMaker();
        game = Game.of("title", 8, false, NORMAL, GameType.SEALED, createTestsSets(), "authToken", "");
        game.setPlayers(new ArrayList<>(Collections.singletonList(Player.of("userId", "name", false))));
    }


    @Test
    public void createPools() {
        final List<Booster> boosters = normalPoolMaker.createPools(game);
        Assert.assertTrue(!boosters.isEmpty());
    }

    private List<MTGSet> createTestsSets() {
        return Arrays.asList(makeSet(), makeSet(), makeSet());
    }

    private MTGSet makeSet() {
        final MTGSet set = new MTGSet();
        set.setCode("testSet");
        set.setName("testSet");
        set.setCards(
            Stream.of(
                makeTestCards(COMMON),
                makeTestCards(UNCOMMON),
                makeTestCards(RARE),
                makeTestCards(MYTHIC_RARE),
                makeTestCards(SPECIAL)
            ).flatMap(Collection::stream).collect(Collectors.toSet())
        );
        return set;
    }

    private List<MTGCard> makeTestCards(Rarity rarity) {
        final List<MTGCard> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            final MTGCard card = new MTGCard();
            card.setRarity(rarity);
            card.setName(rarity.toString() + " " + i);
            card.setId(rarity.toString() + " " + i);
            list.add(card);
        }
        return list;
    }
}

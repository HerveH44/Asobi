package com.hhuneau.asobi.game.pool;

import com.hhuneau.asobi.game.Player;
import com.hhuneau.asobi.game.sets.MTGCard;
import com.hhuneau.asobi.game.sets.MTGSet;
import com.hhuneau.asobi.game.sets.Rarity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hhuneau.asobi.game.sets.Rarity.*;

public class PoolMakerTest {

    private PoolMaker poolMaker;
    private Set<Player> players;
    private List<MTGSet> sets;


    @Before
    public void setUp() {
        poolMaker = new PoolMaker();
        players = new HashSet<Player>(Arrays.asList(new Player("test", null)));
        sets = createTestsSets();
    }


    @Test
    public void createPools() {
        final List<Pool> pools = poolMaker.createPools(players, sets);
        Assert.assertTrue(!pools.isEmpty());
    }

    private List<MTGSet> createTestsSets() {
        return Arrays.asList(makeSet(),makeSet(),makeSet());
    }

    private MTGSet makeSet() {
        final MTGSet set = new MTGSet();
        set.setCode("testSet");
        set.setName("testSet");
        final List<MTGCard> cards = new ArrayList<>();
        set.setCards(
            Stream.of(
                makeTestCards(15, COMMON),
                makeTestCards(15, UNCOMMON),
                makeTestCards(15, RARE),
                makeTestCards(15, MYTHIC_RARE),
                makeTestCards(15, SPECIAL)
            ).flatMap(Collection::stream).collect(Collectors.toSet())
        );
        return set;
    }

    private List<MTGCard> makeTestCards(int number, Rarity rarity) {
        final List<MTGCard> list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            final MTGCard card = new MTGCard();
            card.setRarity(rarity);
            card.setName(rarity.toString() + " " + i);
            card.setId(rarity.toString() + " " + i);
            list.add(card);
        }
        return list;
    }
}

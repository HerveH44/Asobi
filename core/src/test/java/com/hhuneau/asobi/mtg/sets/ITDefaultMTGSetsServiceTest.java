package com.hhuneau.asobi.mtg.sets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ITDefaultMTGSetsServiceTest {


    @Autowired
    private MTGSetRepository setRepository;

    @Autowired
    private MTGCardRepository cardRepository;

    @Test
    public void canSaveSetWithOneCard() {
        final MTGCard card = new MTGCard();
        card.setId("idTest");

        final MTGSet set = new MTGSet();
        set.setCode("idSet");
        final Set<MTGCard> cards = new HashSet<>();
        cards.add(card);
        set.setCards(cards);

        setRepository.save(set);
        Assert.isTrue(!cardRepository.findAll().isEmpty(), "cardRepository must null be empty");
        setRepository.save(set);
    }
}

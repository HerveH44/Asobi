package com.hhuneau.asobi.mtg.sets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhuneau.asobi.SetsPopulator;
import com.hhuneau.asobi.mtg.sets.booster.Slot;
import com.hhuneau.asobi.mtg.sets.card.MTGCard;
import com.hhuneau.asobi.mtg.sets.card.MTGCardRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.hhuneau.asobi.mtg.sets.booster.SlotType.COMMON;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class ITDefaultMTGSetsServiceTest {


    @Autowired
    private MTGSetRepository setRepository;

    @Autowired
    private MTGCardRepository cardRepository;

    @MockBean
    private SetsPopulator setsPopulator;

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
    }


    @Test
    public void canSaveAnEntireSet() throws IOException {
        final File file = new File(ITDefaultMTGSetsServiceTest.class.getResource("m19.json").getFile());

        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, MTGSet> sets = mapper.readValue(file, new TypeReference<Map<String, MTGSet>>() {});

        sets.forEach((setName, set) -> {
            try {
                setRepository.save(set);
            } catch (Exception e) {
            }
        });

        final List<Slot> slotList = setRepository.findById("M19").get().getSlotList();
        Assert.isTrue(slotList.contains(List.of(COMMON)), "slotList must not be empty");
    }
}

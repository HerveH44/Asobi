package com.hhuneau.asobi.mtg.sets;

import java.util.List;
import java.util.Optional;

public interface MTGSetsService {

    void saveSet(MTGSet set);

    List<MTGSet> getSets();

    Optional<MTGSet> getSet(String setCode);

    List<MTGSet> getSets(List<String> sets);
}

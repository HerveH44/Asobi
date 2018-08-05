package com.hhuneau.asobi.mtg.game;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.hhuneau.asobi.mtg.game.Timer.*;
import static java.util.Map.entry;

public class TimeProducer {

    static private final Map<Timer, List<Integer>> map = Map.ofEntries(
        entry(Fast, Arrays.asList(40, 40, 35, 30, 25, 25, 20, 20, 15, 10, 10, 5, 5, 5, 5)),
        entry(Slow, Arrays.asList(75, 70, 65, 60, 55, 50, 45, 40, 35, 30, 25, 20, 15, 12, 10)),
        entry(Leisurely, Arrays.asList(90, 85, 80, 75, 70, 65, 60, 55, 50, 45, 40, 35, 30, 25)),
        entry(Moderate, Arrays.asList(55, 51, 47, 43, 38, 34, 30, 26, 22, 18, 14, 13, 11, 9))
    );

    static public int calc(Timer timer, int pick) {
        final List<Integer> times = map.getOrDefault(timer, List.of(0));
        final int correctedPick = pick - 1; // to adjust pick with index of map

        // If pick is out of scheduled times
        if (pick > times.size()) {
            return times.get(times.size() - 1);
        }

        return times.get(correctedPick);
    }
}

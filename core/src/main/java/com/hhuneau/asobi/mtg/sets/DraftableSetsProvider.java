package com.hhuneau.asobi.mtg.sets;

import java.util.List;
import java.util.Map;

public interface DraftableSetsProvider {

    Map<String, List<SetDTO>> getDraftableSets();
}

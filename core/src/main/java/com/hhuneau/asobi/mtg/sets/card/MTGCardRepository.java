package com.hhuneau.asobi.mtg.sets.card;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface MTGCardRepository extends JpaRepository<MTGCard, String> {

    // MultiverseidAsc gets the first printed version of the cardName
    Stream<MTGCard> findTop2ByNameLikeOrderByMultiverseidAsc(String name);
}

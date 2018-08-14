package com.hhuneau.asobi.mtg.sets;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MTGSetRepository extends JpaRepository<MTGSet, String> {

    @Query(value = "select distinct code from MTGSet where type in ('expansion', 'core') and release_date >= '2003-07-27'")
    List<String> findModernSetCodes();

    @Query(value = "select distinct code from MTGSet where type in ('expansion', 'core')")
    List<String> findTotalChaosSetCodes();
}

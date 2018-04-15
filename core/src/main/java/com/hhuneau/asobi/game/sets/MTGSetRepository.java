package com.hhuneau.asobi.game.sets;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MTGSetRepository extends JpaRepository<MTGSet, String> {
}

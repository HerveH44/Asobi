package com.hhuneau.asobi.game.pool;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoolRepository extends JpaRepository<Booster, Long> {
}

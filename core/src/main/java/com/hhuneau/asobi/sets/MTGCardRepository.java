package com.hhuneau.asobi.sets;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MTGCardRepository extends JpaRepository<MTGCard, String> {
}

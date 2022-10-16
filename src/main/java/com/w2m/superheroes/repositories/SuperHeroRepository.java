package com.w2m.superheroes.repositories;

import com.w2m.superheroes.models.SuperHero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SuperHeroRepository extends JpaRepository<SuperHero, Long> {

    @Query("select s from SuperHero s where lower(s.name) like lower(?1)")
    List<SuperHero> findByPattern(String pattern);

}

package com.w2m.superheroes.services;

import com.w2m.superheroes.models.SuperHero;

import java.util.List;

public interface SuperHeroService {

    List<SuperHero> findAll();

    SuperHero findById(Long id);

    List<SuperHero> findByPattern(String pattern);

    SuperHero save(SuperHero superHero);

    void deleteById(Long id);

}

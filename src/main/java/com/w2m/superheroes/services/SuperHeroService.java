package com.w2m.superheroes.services;

import com.w2m.superheroes.models.SuperHero;

import java.util.List;

public interface SuperHeroService {

    List<SuperHero> findAll();

    SuperHero findById(Long id);

    List<SuperHero> findByPattern(String pattern);

    SuperHero update(SuperHero superHero);

    void deleteById(Long id);

}

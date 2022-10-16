package com.w2m.superheroes.services;

import com.w2m.superheroes.exceptions.W2M_Exception;
import com.w2m.superheroes.models.SuperHero;
import com.w2m.superheroes.repositories.SuperHeroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuperHeroServiceImpl implements SuperHeroService {

    private final SuperHeroRepository repository;

    public SuperHeroServiceImpl(SuperHeroRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<SuperHero> findAll() {
        return this.repository.findAll();
    }

    @Override
    public SuperHero findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new W2M_Exception(String.format("Super Hero not found with id %d", id)));
    }

    @Override
    public List<SuperHero> findByPattern(String pattern) {
        return this.repository.findByPattern(String.format("%%%s%%", pattern));
    }

    @Override
    public SuperHero update(SuperHero superHero) {
        return this.repository.save(superHero);
    }

    @Override
    public void deleteById(Long id) {
        this.repository.deleteById(id);
    }

}

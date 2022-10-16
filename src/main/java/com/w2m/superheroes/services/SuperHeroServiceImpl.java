package com.w2m.superheroes.services;

import com.w2m.superheroes.exceptions.W2M_Exception;
import com.w2m.superheroes.models.SuperHero;
import com.w2m.superheroes.repositories.SuperHeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SuperHeroServiceImpl implements SuperHeroService {

    @Autowired
    private SuperHeroRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<SuperHero> findAll() {
        return this.repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public SuperHero findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new W2M_Exception(String.format("Super Hero not found with id %d", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuperHero> findByPattern(String pattern) {
        return this.repository.findByPattern(String.format("%%%s%%", pattern));
    }

    @Override
    @Transactional
    public SuperHero update(SuperHero superHero) {
        return this.repository.save(superHero);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.repository.deleteById(id);
    }

}

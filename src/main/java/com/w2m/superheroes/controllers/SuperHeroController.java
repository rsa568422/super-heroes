package com.w2m.superheroes.controllers;

import com.w2m.superheroes.exceptions.W2M_Exception;
import com.w2m.superheroes.models.SuperHero;
import com.w2m.superheroes.services.SuperHeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/super-heroes")
public class SuperHeroController {

    @Autowired
    private SuperHeroService service;

    @GetMapping
    @ResponseStatus(OK)
    public List<SuperHero> findAll() {
        return this.service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        SuperHero superHero;
        superHero = this.service.findById(id);
        return ResponseEntity.ok(superHero);
    }

    @GetMapping("/findByPattern/{pattern}")
    public List<SuperHero> findByPattern(@PathVariable String pattern) {
        return this.service.findByPattern(pattern);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public SuperHero update(@RequestBody SuperHero superHero) {
        return this.service.update(superHero);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        this.service.deleteById(id);
    }

}

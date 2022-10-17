package com.w2m.superheroes;

import com.w2m.superheroes.models.SuperHero;
import com.w2m.superheroes.repositories.SuperHeroRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static com.w2m.superheroes.data.Data.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class JpaIntegrationTest {

    @Autowired
    private SuperHeroRepository repository;

    @Test
    void test_findAll() {
        List<SuperHero> superHeroes = this.repository.findAll();

        assertAll(
                () -> assertEquals(6, superHeroes.size()),
                () -> assertTrue(superHeroes.containsAll(SUPER_HEROES())),
                () -> assertTrue(superHeroes.contains(new SuperHero(4L, "Hulk"))),
                () -> assertTrue(superHeroes.contains(new SuperHero(5L, "Ironman"))),
                () -> assertTrue(superHeroes.contains(new SuperHero(6L, "El tío la vara")))
        );
    }

    @Test
    void test_findById_id1() {
        Optional<SuperHero> superHero = this.repository.findById(1L);

        assertAll(
                () -> assertTrue(superHero.isPresent()),
                () -> assertEquals(SPIDERMAN(), superHero.get())
        );
    }

    @Test
    void test_findById_id7() {
        Optional<SuperHero> superHero = this.repository.findById(7L);

        assertTrue(superHero.isEmpty());
    }

    @Test
    void test_findByPattern_man() {
        List<SuperHero> superHeroes = this.repository.findByPattern("%man%");

        assertAll(
                () -> assertEquals(4, superHeroes.size()),
                () -> assertTrue(superHeroes.contains(SPIDERMAN())),
                () -> assertTrue(superHeroes.contains(SUPERMAN())),
                () -> assertTrue(superHeroes.contains(MANOLITO())),
                () -> assertTrue(superHeroes.contains(new SuperHero(5L, "Ironman")))
        );
    }

    @Test
    void test_findByPattern_rman() {
        List<SuperHero> superHeroes = this.repository.findByPattern("%rman%");

        assertAll(
                () -> assertEquals(2, superHeroes.size()),
                () -> assertTrue(superHeroes.contains(SPIDERMAN())),
                () -> assertTrue(superHeroes.contains(SUPERMAN()))
        );
    }

    @Test
    void test_save() {
        SuperHero superHero = new SuperHero(7L, "Super mock");

        SuperHero actual = this.repository.save(superHero);

        assertAll(
                () -> assertEquals(7L, actual.getId()),
                () -> assertEquals("Super mock", actual.getName()),
                () -> assertEquals(superHero, actual)
        );

        actual.setName("Supermock");

        SuperHero updated = this.repository.save(actual);

        assertAll(
                () -> assertEquals(7L, updated.getId()),
                () -> assertEquals("Supermock", updated.getName()),
                () -> assertNotEquals(superHero.getName(), updated.getName())
        );
    }

    @Test
    void test_deleteById() {
        this.repository.deleteById(1L);

        List<SuperHero> superHeroes = this.repository.findAll();

        assertAll(
                () -> assertEquals(5, superHeroes.size()),
                () -> assertFalse(superHeroes.contains(SPIDERMAN())),
                () -> assertTrue(superHeroes.contains(SUPERMAN())),
                () -> assertTrue(superHeroes.contains(MANOLITO())),
                () -> assertTrue(superHeroes.contains(new SuperHero(4L, "Hulk"))),
                () -> assertTrue(superHeroes.contains(new SuperHero(5L, "Ironman"))),
                () -> assertTrue(superHeroes.contains(new SuperHero(6L, "El tío la vara")))
        );
    }

}

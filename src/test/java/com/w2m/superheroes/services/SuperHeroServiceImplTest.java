package com.w2m.superheroes.services;

import com.w2m.superheroes.exceptions.W2M_Exception;
import com.w2m.superheroes.models.SuperHero;
import com.w2m.superheroes.repositories.SuperHeroRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.w2m.superheroes.data.Data.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class SuperHeroServiceImplTest {

    @InjectMocks
    private SuperHeroServiceImpl service;

    @Mock
    private SuperHeroRepository repository;

    @Test
    void test_findAll() {
        when(this.repository.findAll()).thenReturn(SUPER_HEROES());

        List<SuperHero> actual = this.service.findAll();

        assertEquals(SUPER_HEROES(), actual);
        verify(this.repository).findAll();
    }

    @Test
    void test_findById_id1() {
        when(this.repository.findById(argThat(arg -> arg.equals(1L)))).thenReturn(Optional.of(SPIDERMAN()));

        SuperHero actual = this.service.findById(1L);

        assertAll(
                () -> assertEquals(SPIDERMAN(), actual),
                () -> assertEquals(SPIDERMAN().hashCode(), actual.hashCode())
        );
        verify(this.repository).findById(1L);
    }

    @Test
    void test_findById_id7() {
        when(this.repository.findById(argThat(arg -> arg.equals(7L)))).thenThrow(new W2M_Exception("MOCK_FIND_ERROR"));

        Exception actual = assertThrows(W2M_Exception.class, () -> this.service.findById(7L));
        assertEquals(W2M_Exception.class, actual.getClass());

        verify(this.repository).findById(7L);
    }

    @Test
    void test_findByPattern_man() {
        when(this.repository.findByPattern(argThat(arg -> arg.equals("%man%")))).thenReturn(SUPER_HEROES());

        List<SuperHero> actual = this.service.findByPattern("man");

        assertEquals(SUPER_HEROES(), actual);
        verify(this.repository).findByPattern("%man%");
    }

    @Test
    void test_findByPattern_rman() {
        when(this.repository.findByPattern(argThat("%rman%"::equals)))
                .thenReturn(Arrays.asList(SPIDERMAN(), SUPERMAN()));

        List<SuperHero> actual = this.service.findByPattern("rman");

        assertEquals(Arrays.asList(SPIDERMAN(), SUPERMAN()), actual);
        verify(this.repository).findByPattern("%rman%");
    }

    @Test
    void test_save() {
        SuperHero hero = new SuperHero(1L, "Hombre araña");

        when(this.repository.save(argThat(arg -> arg.getId().equals(1L)))).thenReturn(hero);

        SuperHero actual = this.service.save(hero);

        assertEquals(hero, actual);
        verify(this.repository).save(hero);
    }

    @Test
    void test_deleteById() {
        doNothing().when(this.repository).deleteById(argThat(arg -> arg.equals(1L)));

        this.service.deleteById(1L);

        verify(this.repository).deleteById(1L);
    }

}
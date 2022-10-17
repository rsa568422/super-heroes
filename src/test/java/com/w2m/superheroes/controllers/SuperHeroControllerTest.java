package com.w2m.superheroes.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.w2m.superheroes.exceptions.W2M_Exception;
import com.w2m.superheroes.models.SuperHero;
import com.w2m.superheroes.services.SuperHeroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static com.w2m.superheroes.data.Data.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SuperHeroController.class)
class SuperHeroControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SuperHeroService service;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        this.mapper = new ObjectMapper();
    }

    @Test
    void test_findAll() throws Exception {
        when(this.service.findAll()).thenReturn(SUPER_HEROES());

        this.mvc.perform(get("/super-heroes")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Spiderman"))
                .andExpect(jsonPath("$[1].name").value("Superman"))
                .andExpect(jsonPath("$[2].name").value("Manolito el fuerte"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(content().json(this.mapper.writeValueAsString(SUPER_HEROES())));

        verify(this.service).findAll();
    }

    @Test
    void test_findById_id3() throws Exception {
        when(this.service.findById(argThat(arg -> arg.equals(3L)))).thenReturn(MANOLITO());

        this.mvc.perform(get("/super-heroes/3")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Manolito el fuerte"));

        verify(this.service).findById(3L);
    }

    @Test
    void test_findById_id7() throws Exception {
        when(this.service.findById(argThat(arg -> Long.valueOf(3L).compareTo(arg) < 0))).thenThrow(new W2M_Exception("MOCK_NOT_FOUND"));

        this.mvc.perform(get("/super-heroes/7")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(this.service).findById(7L);
    }

    @Test
    void test_findByPattern_man() throws Exception {
        when(this.service.findByPattern(argThat("man"::equals))).thenReturn(SUPER_HEROES());

        this.mvc.perform(get("/super-heroes/findByPattern/man")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Spiderman"))
                .andExpect(jsonPath("$[1].name").value("Superman"))
                .andExpect(jsonPath("$[2].name").value("Manolito el fuerte"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(content().json(this.mapper.writeValueAsString(SUPER_HEROES())));

        verify(this.service).findByPattern("man");
    }

    @Test
    void test_findByPattern_erman() throws Exception {
        when(this.service.findByPattern(argThat("erman"::equals))).thenReturn(Arrays.asList(SPIDERMAN(), SUPERMAN()));

        this.mvc.perform(get("/super-heroes/findByPattern/erman")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Spiderman"))
                .andExpect(jsonPath("$[1].name").value("Superman"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(this.mapper.writeValueAsString(Arrays.asList(SPIDERMAN(), SUPERMAN()))));

        verify(this.service).findByPattern("erman");
    }

    @Test
    void test_save() throws Exception {
        SuperHero actual = new SuperHero(1L, "Hombre araña");

        when(this.service.save(argThat(arg -> arg.getId().equals(1L)))).thenReturn(actual);

        this.mvc.perform(post("/super-heroes")
                        .contentType(APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(actual)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Hombre araña"))
                .andExpect(content().json(this.mapper.writeValueAsString(actual)));

        verify(this.service).save(actual);
    }

    @Test
    void test_deleteById() throws Exception {
        doNothing().when(this.service).deleteById(any());

        this.mvc.perform(delete("/super-heroes/1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(this.service).deleteById(1L);
    }

}
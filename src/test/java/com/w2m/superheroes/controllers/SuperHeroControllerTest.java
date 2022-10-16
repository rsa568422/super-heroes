package com.w2m.superheroes.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.w2m.superheroes.Datos;
import com.w2m.superheroes.models.Cuenta;
import com.w2m.superheroes.models.TransaccionDto;
import com.w2m.superheroes.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SuperHeroController.class)
class SuperHeroControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CuentaService cuentaService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void testDetalle() throws Exception {
        when(this.cuentaService.findById(1L)).thenReturn(Datos.crearCuenta001().orElseThrow());

        this.mvc.perform(get("/api/cuentas/1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.persona").value("Andrés"))
                .andExpect(jsonPath("$.saldo").value("1000"));

        verify(this.cuentaService).findById(1L);
    }

    @Test
    void testTransferir() throws Exception {
        TransaccionDto dto = new TransaccionDto();
        dto.setBancoId(1L);
        dto.setCuentaOrigenId(1L);
        dto.setCuentaDestinoId(2L);
        dto.setMonto(new BigDecimal("100"));

        System.out.println(this.objectMapper.writeValueAsString(dto));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("mensaje", "Transferencia realizada con éxito");
        response.put("transaccion", dto);

        System.out.println(this.objectMapper.writeValueAsString(response));

        this.mvc.perform(post("/api/cuentas/transferir")
                .contentType(APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.mensaje").value("Transferencia realizada con éxito"))
                .andExpect(jsonPath("$.transaccion.cuentaOrigenId").value(dto.getCuentaOrigenId()))
                .andExpect(content().json(this.objectMapper.writeValueAsString(response)));
    }

    @Test
    void testListar() throws Exception {
        List<Cuenta> cuentas = Arrays.asList(Datos.crearCuenta001().orElseThrow(), Datos.crearCuenta002().orElseThrow());
        when(this.cuentaService.findAll()).thenReturn(cuentas);

        this.mvc.perform(get("/api/cuentas")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].persona").value("Andrés"))
                .andExpect(jsonPath("$[0].saldo").value("1000"))
                .andExpect(jsonPath("$[1].persona").value("Roberto"))
                .andExpect(jsonPath("$[1].saldo").value("2000"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(this.objectMapper.writeValueAsString(cuentas)));
        verify(this.cuentaService).findAll();
    }

    @Test
    void testGuardar() throws Exception {
        Cuenta cuenta = new Cuenta(null, "Pepe", new BigDecimal("3000"));
        when(this.cuentaService.save(any())).then(invocation -> {
            Cuenta c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });

        this.mvc.perform(post("/api/cuentas")
                .contentType(APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(cuenta)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.persona", is("Pepe")))
                .andExpect(jsonPath("$.saldo", is(3000)));
        verify(this.cuentaService).save(any());
    }

}
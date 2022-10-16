package com.w2m.superheroes;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.w2m.superheroes.models.Cuenta;
import com.w2m.superheroes.repositories.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integracion_jpa")
@DataJpaTest
public class IntegracionJpaTest {

    @Autowired
    CuentaRepository cuentaRepository;

    @Test
    void testFindById() {
        Optional<Cuenta> cuenta = this.cuentaRepository.findById(1L);

        assertTrue(cuenta.isPresent());
        assertEquals("Andrés", cuenta.orElseThrow().getPersona());
    }

    @Test
    void testFindByPersona() {
        Optional<Cuenta> cuenta = this.cuentaRepository.findByPersona("Andrés");

        assertTrue(cuenta.isPresent());
        assertEquals("1000.00", cuenta.orElseThrow().getSaldo().toPlainString());
    }

    @Test
    void testFindByPersonaThrowException() {
        Optional<Cuenta> cuenta = this.cuentaRepository.findByPersona("John");

        assertThrows(NoSuchElementException.class, cuenta::orElseThrow);
        assertFalse(cuenta.isPresent());
    }

    @Test
    void testFindAll() {
        List<Cuenta> cuentas = this.cuentaRepository.findAll();

        assertFalse(cuentas.isEmpty());
        assertEquals(2, cuentas.size());
    }

    @Test
    void testSave() {
        Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal("3000"));

        Cuenta cuenta = this.cuentaRepository.save(cuentaPepe);

        assertEquals("Pepe", cuenta.getPersona());
        assertEquals("3000", cuenta.getSaldo().toPlainString());
        //assertEquals(3, cuenta.getId());
    }

    @Test
    void testUpdate() {
        Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal("3000"));

        Cuenta cuenta = this.cuentaRepository.save(cuentaPepe);

        assertEquals("Pepe", cuenta.getPersona());
        assertEquals("3000", cuenta.getSaldo().toPlainString());

        cuenta.setSaldo(new BigDecimal("3800"));

        Cuenta cuentaActualizada = this.cuentaRepository.save(cuenta);

        assertEquals("Pepe", cuentaActualizada.getPersona());
        assertEquals("3800", cuentaActualizada.getSaldo().toPlainString());
    }

    @Test
    void testDelete() {
        Cuenta cuenta = this.cuentaRepository.findById(2L).orElseThrow();

        assertEquals("Roberto", cuenta.getPersona());

        this.cuentaRepository.delete(cuenta);

        assertThrows(NoSuchElementException.class, () -> this.cuentaRepository.findById(2L).orElseThrow());
        assertEquals(1, this.cuentaRepository.findAll().size());
    }

}

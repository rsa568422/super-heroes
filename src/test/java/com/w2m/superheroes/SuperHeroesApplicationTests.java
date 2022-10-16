package com.w2m.superheroes;

import org.junit.jupiter.api.Test;
import com.w2m.superheroes.exceptions.DineroInsuficienteException;
import com.w2m.superheroes.models.Banco;
import com.w2m.superheroes.models.Cuenta;
import com.w2m.superheroes.repositories.BancoRepository;
import com.w2m.superheroes.repositories.CuentaRepository;
import com.w2m.superheroes.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SuperHeroesApplicationTests {

	@Autowired
	CuentaService service;

	@MockBean
	CuentaRepository cuentaRepository;

	@MockBean
	BancoRepository bancoRepository;

	@Test
	void contextLoads() {
		Optional<Cuenta> cuentaOrigen = Datos.crearCuenta001();
		Optional<Cuenta> cuentaDestino = Datos.crearCuenta002();
		Optional<Banco> banco = Datos.crearBanco();

		when(this.cuentaRepository.findById(1L)).thenReturn(cuentaOrigen);
		when(this.cuentaRepository.findById(2L)).thenReturn(cuentaDestino);
		when(this.bancoRepository.findById(1L)).thenReturn(banco);

		BigDecimal saldoOrigen = this.service.revisarSaldo(1L);
		BigDecimal saldoDestino = this.service.revisarSaldo(2L);

		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());

		this.service.transferir(1L, 1L, 2L, new BigDecimal("100"));

		saldoOrigen = this.service.revisarSaldo(1L);
		saldoDestino = this.service.revisarSaldo(2L);

		assertEquals("900", saldoOrigen.toPlainString());
		assertEquals("2100", saldoDestino.toPlainString());

		int total = this.service.revisarTotalTransferencias(1L);

		assertEquals(1, total);

		verify(this.cuentaRepository, times(3)).findById(1L);
		verify(this.cuentaRepository, times(3)).findById(2L);
		verify(this.cuentaRepository, times(2)).save(any(Cuenta.class));

		verify(this.bancoRepository, times(2)).findById(1L);
		verify(this.bancoRepository).save(any(Banco.class));

		verify(this.cuentaRepository, times(6)).findById(anyLong());
		verify(this.cuentaRepository, never()).findAll();
	}

	@Test
	void contextLoads2() {
		Optional<Cuenta> cuentaOrigen = Datos.crearCuenta001();
		Optional<Cuenta> cuentaDestino = Datos.crearCuenta002();
		Optional<Banco> banco = Datos.crearBanco();

		when(this.cuentaRepository.findById(1L)).thenReturn(cuentaOrigen);
		when(this.cuentaRepository.findById(2L)).thenReturn(cuentaDestino);
		when(this.bancoRepository.findById(1L)).thenReturn(banco);

		BigDecimal saldoOrigen = this.service.revisarSaldo(1L);
		BigDecimal saldoDestino = this.service.revisarSaldo(2L);

		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());

		assertThrows(DineroInsuficienteException.class, () -> this.service.transferir(1L, 1L, 2L, new BigDecimal("1200")));

		saldoOrigen = this.service.revisarSaldo(1L);
		saldoDestino = this.service.revisarSaldo(2L);

		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());

		int total = this.service.revisarTotalTransferencias(1L);

		assertEquals(0, total);

		verify(this.cuentaRepository, times(3)).findById(1L);
		verify(this.cuentaRepository, times(2)).findById(2L);
		verify(this.cuentaRepository, never()).save(any(Cuenta.class));

		verify(this.bancoRepository, times(1)).findById(1L);
		verify(this.bancoRepository, never()).save(any(Banco.class));

		verify(this.cuentaRepository, times(5)).findById(anyLong());
		verify(this.cuentaRepository, never()).findAll();
	}

	@Test
	void contextLoads3() {
		when(this.cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta001());

		Cuenta cuenta1 = this.service.findById(1L);
		Cuenta cuenta2 = this.service.findById(1L);

		assertSame(cuenta1, cuenta2);
		assertEquals(cuenta1, cuenta2);
		assertEquals("Andrés", cuenta1.getPersona());
		assertEquals("Andrés", cuenta2.getPersona());
		verify(this.cuentaRepository, times(2)).findById(1L);
	}

	@Test
	void testFindAll() {
		List<Cuenta> datos = Arrays.asList(Datos.crearCuenta001().orElseThrow(), Datos.crearCuenta002().orElseThrow());
		when(this.cuentaRepository.findAll()).thenReturn(datos);

		List<Cuenta> cuentas = this.service.findAll();

		assertFalse(cuentas.isEmpty());
		assertEquals(2, cuentas.size());
		assertTrue(cuentas.contains(Datos.crearCuenta002().orElseThrow()));
		verify(this.cuentaRepository).findAll();
	}

	@Test
	void testSave() {
		Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal("3000"));
		when(this.cuentaRepository.save(any())).then(invocation -> {
			Cuenta c = invocation.getArgument(0);
			c.setId(3L);
			return c;
		});

		Cuenta cuenta = this.service.save(cuentaPepe);

		assertEquals("Pepe", cuenta.getPersona());
		assertEquals(3L, cuenta.getId());
		assertEquals("3000", cuenta.getSaldo().toPlainString());

		verify(this.cuentaRepository).save(any());
	}

}

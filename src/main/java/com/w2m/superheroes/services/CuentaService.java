package com.w2m.superheroes.services;

import com.w2m.superheroes.models.Cuenta;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {

    List<Cuenta> findAll();

    Cuenta findById(Long id);

    Cuenta save(Cuenta cuenta);

    void deleteById(Long id);

    int revisarTotalTransferencias(Long bancoId);

    BigDecimal revisarSaldo(Long cuentaId);

    void transferir(Long bancoId, Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto);

}

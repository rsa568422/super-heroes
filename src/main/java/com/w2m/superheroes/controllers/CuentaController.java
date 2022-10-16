package com.w2m.superheroes.controllers;

import static org.springframework.http.HttpStatus.*;

import com.w2m.superheroes.models.Cuenta;
import com.w2m.superheroes.models.TransaccionDto;
import com.w2m.superheroes.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/super-heroes")
public class CuentaController {

    @Autowired
    CuentaService cuentaService;

    @GetMapping
    @ResponseStatus(OK)
    public List<Cuenta> findAll() {
        return this.cuentaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Cuenta cuenta;
        try {
            cuenta = this.cuentaService.findById(id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cuenta);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Cuenta guardar(@RequestBody Cuenta cuenta) {
        return this.cuentaService.save(cuenta);
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestBody TransaccionDto dto) {
        this.cuentaService.transferir(dto.getBancoId(), dto.getCuentaOrigenId(), dto.getCuentaDestinoId(), dto.getMonto());

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("mensaje", "Transferencia realizada con éxito");
        response.put("transaccion", dto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        this.cuentaService.deleteById(id);
    }

}

package com.tiendatcg.ms_precios.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendatcg.ms_precios.dto.PrecioRequestDTO;
import com.tiendatcg.ms_precios.dto.PrecioResponseDTO;
import com.tiendatcg.ms_precios.service.PrecioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/precios")
@RequiredArgsConstructor
public class PrecioController {
    private final PrecioService precioService;

    @GetMapping
    public ResponseEntity<List<PrecioResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(precioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrecioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return precioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/carta/{idCarta}")
    public ResponseEntity<List<PrecioResponseDTO>> obtenerHistorialPorCarta(@PathVariable Long idCarta) {
        return ResponseEntity.ok(precioService.obtenerHistorialPorCarta(idCarta));
    }

    @PostMapping
    public ResponseEntity<PrecioResponseDTO> crear(@Valid @RequestBody PrecioRequestDTO dto) {
        return ResponseEntity.status(201).body(precioService.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrecioResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody PrecioRequestDTO dto) {
        return precioService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (precioService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        precioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

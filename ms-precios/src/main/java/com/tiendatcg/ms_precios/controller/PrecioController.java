package com.tiendatcg.ms_precios.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ResponseEntity<List<PrecioResponseDTO>> obtenerTodos(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(precioService.obtenerTodos(token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrecioResponseDTO> obtenerPorId(@PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        return precioService.obtenerPorId(id, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/carta/{idCarta}")
    public ResponseEntity<List<PrecioResponseDTO>> obtenerHistorialPorCarta(@PathVariable Long idCarta,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(precioService.obtenerHistorialPorCarta(idCarta, token));
    }

    @PostMapping
    public ResponseEntity<PrecioResponseDTO> crear(@Valid @RequestBody PrecioRequestDTO dto,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(201).body(precioService.guardar(dto, token));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrecioResponseDTO> actualizar(@PathVariable Long id,
            @Valid @RequestBody PrecioRequestDTO dto,
            @RequestHeader("Authorization") String token) {
        return precioService.actualizar(id, dto, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        if (precioService.obtenerPorId(id, token).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        precioService.eliminar(id, token);
        return ResponseEntity.noContent().build();
    }
}

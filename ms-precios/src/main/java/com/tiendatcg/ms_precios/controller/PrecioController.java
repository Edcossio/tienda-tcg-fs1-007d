package com.tiendatcg.ms_precios.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<PrecioResponseDTO>> obtenerTodos(
            @RequestHeader("X-User-Rol") String rol) {
        return ResponseEntity.ok(precioService.obtenerTodos(rol));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrecioResponseDTO> obtenerPorId(
            @PathVariable Long id,
            @RequestHeader("X-User-Rol") String rol) {
        return precioService.obtenerPorId(id, rol)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/carta/{idCarta}")
    public ResponseEntity<List<PrecioResponseDTO>> obtenerHistorialPorCarta(
            @PathVariable Long idCarta,
            @RequestHeader("X-User-Rol") String rol) {
        return ResponseEntity.ok(precioService.obtenerHistorialPorCarta(idCarta, rol));
    }

    @PostMapping
    public ResponseEntity<PrecioResponseDTO> crear(
            @Valid @RequestBody PrecioRequestDTO dto,
            @RequestHeader("X-User-Rol") String rol) {
        return ResponseEntity.status(201).body(precioService.guardar(dto, rol));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrecioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PrecioRequestDTO dto,
            @RequestHeader("X-User-Rol") String rol) {
        return precioService.actualizar(id, dto, rol)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @RequestHeader("X-User-Rol") String rol) {
        precioService.eliminar(id, rol);
        return ResponseEntity.noContent().build();
    }
}
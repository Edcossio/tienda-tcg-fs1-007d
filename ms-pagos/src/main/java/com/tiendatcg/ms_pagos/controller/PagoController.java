package com.tiendatcg.ms_pagos.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tiendatcg.ms_pagos.dto.PagoRequestDTO;
import com.tiendatcg.ms_pagos.dto.PagoResponseDTO;
import com.tiendatcg.ms_pagos.service.PagoService;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    
    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> obtenerTodos(
            @RequestHeader("X-User-Rol") String rol) { 
        return ResponseEntity.ok(pagoService.obtenerTodos(rol));
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> obtenerPorId(
            @PathVariable Long id,
            @RequestHeader("X-User-Rol") String rol) { 
        return pagoService.obtenerPorId(id, rol)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/pagos/pedido/{idPedido}
    @GetMapping("/pedido/{idPedidoRef}") 
    public ResponseEntity<PagoResponseDTO> obtenerPorPedido(
            @PathVariable Long idPedidoRef,
            @RequestHeader("X-User-Rol") String rol) { 
        return pagoService.obtenerPorPedido(idPedidoRef, rol) 
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PagoResponseDTO> procesarPago(
            @RequestHeader("X-User-Rol") String rol, 
            @Valid @RequestBody PagoRequestDTO dto) {
        return ResponseEntity.status(201).body(pagoService.procesarPago(dto, rol));
    }
}
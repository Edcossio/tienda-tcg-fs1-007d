package com.tcgstore.ms_pagos.controller;

import com.tcgstore.ms_pagos.dto.PagoRequestDTO;
import com.tcgstore.ms_pagos.dto.PagoResponseDTO;
import com.tcgstore.ms_pagos.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

private final PagoService pagoService;

    // GET /api/pagos -> Solo personal autorizado (según lógica en service)
    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> obtenerTodos(
            @RequestHeader("Authorization") String token) { // <--- Recibe el token
        return ResponseEntity.ok(pagoService.obtenerTodos(token));
    }

    // GET /api/pagos/{id} -> Un USER solo puede ver su propio pago
    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> obtenerPorId(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        return pagoService.obtenerPorId(id, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/pagos/pedido/{idPedido}
    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<PagoResponseDTO> obtenerPorPedido(
            @PathVariable Long idPedidoRef,
            @RequestHeader("Authorization") String token) {
        // Asumiendo que actualizaste el método en el Service para recibir el token
        return pagoService.obtenerPorPedido(idPedidoRef, token) 
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/pagos -> Crea el pago validando rol y monto
    @PostMapping
    public ResponseEntity<PagoResponseDTO> procesarPago(
            @RequestHeader("Authorization") String token, // <--- Obligatorio para procesar
            @Valid @RequestBody PagoRequestDTO dto) {
        return ResponseEntity.status(201).body(pagoService.procesarPago(dto, token));
    }
}
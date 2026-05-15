package com.tiendatcg.ms_envios.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tiendatcg.ms_envios.dto.EnvioResponseDTO;
import com.tiendatcg.ms_envios.dto.EnvioRequestDTO;
import com.tiendatcg.ms_envios.service.EnvioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/envios")
@RequiredArgsConstructor
public class EnvioController {
    private final EnvioService envioService;

    @GetMapping
    public ResponseEntity<List<EnvioResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(envioService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<EnvioResponseDTO> crearEnvio(@RequestBody EnvioRequestDTO request) {
        return ResponseEntity.status(201).body(envioService.crearEnvio(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnvioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(envioService.obtenerPorId(id));
    }
}
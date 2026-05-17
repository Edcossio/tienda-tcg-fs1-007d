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
    public ResponseEntity<List<EnvioResponseDTO>> obtenerTodos(
            @RequestHeader("X-User-Rol") String rol) { // Inyectamos el rol
        return ResponseEntity.ok(envioService.obtenerTodos(rol));
    }

    @PostMapping
    public ResponseEntity<EnvioResponseDTO> crearEnvio(
            @RequestBody EnvioRequestDTO request,
            @RequestHeader("X-User-Rol") String rol) { // Inyectamos el rol
        return ResponseEntity.status(201).body(envioService.crearEnvio(request, rol));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnvioResponseDTO> obtenerPorId(
            @PathVariable Long id,
            @RequestHeader("X-User-Rol") String rol) { // Inyectamos el rol
        return ResponseEntity.ok(envioService.obtenerPorId(id, rol));
    }
}
package com.tiendatcg.ms_auth.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendatcg.ms_auth.dto.AuthRequestDTO;
import com.tiendatcg.ms_auth.dto.AuthResponseDTO;
import com.tiendatcg.ms_auth.service.AutenticacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AutenticacionController {
    private final AutenticacionService authService;

    // POST /api/auth/registrar -> 201 Created
    @PostMapping("/registrar")
    public ResponseEntity<AuthResponseDTO> registrar(@Valid @RequestBody AuthRequestDTO dto) {
        // @Valid dispara las validaciones como @NotBlank [cite: 80, 149]
        return ResponseEntity.status(201).body(authService.registrar(dto));
    }

    // POST /api/auth/login -> 200 OK
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody Map<String, String> credenciales) {
        // Recibimos un mapa simple para el login
        return ResponseEntity.ok(authService.login(
                credenciales.get("username"),
                credenciales.get("password")));
    }

    @GetMapping("/validar-token")
    public ResponseEntity<AuthResponseDTO> validarToken(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authService.validarToken(token));
    }
}

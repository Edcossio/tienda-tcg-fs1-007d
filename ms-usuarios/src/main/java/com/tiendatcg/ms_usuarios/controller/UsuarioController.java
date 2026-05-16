package com.tiendatcg.ms_usuarios.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tiendatcg.ms_usuarios.dto.UsuarioRequestDTO;
import com.tiendatcg.ms_usuarios.dto.UsuarioResponseDTO;
import com.tiendatcg.ms_usuarios.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodos(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.obtenerTodos(token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id,
                                                           @RequestHeader("Authorization") String token) {
        return usuarioService.obtenerPorId(id, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioRequestDTO dto,
                                                    @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(201).body(usuarioService.guardar(dto, token));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(@PathVariable Long id,
                                                         @Valid @RequestBody UsuarioRequestDTO dto,
                                                         @RequestHeader("Authorization") String token) {
        return usuarioService.actualizar(id, dto, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id,
                                         @RequestHeader("Authorization") String token) {
        usuarioService.eliminar(id, token);
        return ResponseEntity.noContent().build();
    }
}
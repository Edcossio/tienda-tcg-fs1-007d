package com.tiendatcg.ms_usuarios.controller;

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
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodos(
            @RequestHeader("X-User-Rol") String rol) {
        return ResponseEntity.ok(usuarioService.obtenerTodos(rol));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(
            @PathVariable Long id,
            @RequestHeader("X-User-Rol") String rol,
            @RequestHeader(value = "X-User-Id", required = false) Long idUsuarioLogueado) {
        System.out.println(">>> X-User-Rol recibido: [" + rol + "]");
        System.out.println(">>> X-User-Id recibido: [" + idUsuarioLogueado + "]"); // Extraemos el ID
        return usuarioService.obtenerPorId(id, rol, idUsuarioLogueado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(
            @Valid @RequestBody UsuarioRequestDTO dto,
            @RequestHeader("X-User-Rol") String rol) {
        return ResponseEntity.status(201).body(usuarioService.guardar(dto, rol));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO dto,
            @RequestHeader("X-User-Rol") String rol,
            @RequestHeader(value = "X-User-Id", required = false) Long idUsuarioLogueado) { // Extraemos el ID
        return usuarioService.actualizar(id, dto, rol, idUsuarioLogueado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @RequestHeader("X-User-Rol") String rol) {
        usuarioService.eliminar(id, rol);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/validar-user/{id}")
    public ResponseEntity<Boolean> validarUser(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.existePorId(id));
    }
}
package com.tiendatcg.ms_auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tiendatcg.ms_auth.client.UsuarioClient;
import com.tiendatcg.ms_auth.dto.AuthRequestDTO;
import com.tiendatcg.ms_auth.dto.AuthResponseDTO;
import com.tiendatcg.ms_auth.model.Autenticacion;
import com.tiendatcg.ms_auth.repository.AuthRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AutenticacionService {
    // Roles que el sistema reconoce
    private static final java.util.Set<String> ROLES_PERMITIDOS = java.util.Set.of("USER", "EMPLEADO", "ADMIN");

    private final AuthRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final UsuarioClient usuarioClient;
    private final JwtService jwtService;

    public AuthResponseDTO registrar(AuthRequestDTO dto) {

        // 1. Validar que el rol enviado existe en el sistema
        String rolSolicitado = dto.getRol() != null
                ? dto.getRol().toUpperCase().trim()
                : "";

        if (!ROLES_PERMITIDOS.contains(rolSolicitado)) {
            throw new RuntimeException(
                    "Rol inválido. Los roles permitidos son: " + ROLES_PERMITIDOS);
        }

        // 2. Registro público solo puede crear USER
        if ("ADMIN".equals(rolSolicitado) || "EMPLEADO".equals(rolSolicitado)) {
            throw new RuntimeException(
                    "No tienes permisos para registrar un usuario con ese rol.");
        }

        // 3. Verificar que el username no esté ya en uso
        if (repository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException(
                    "El nombre de usuario ya está en uso.");
        }

        // 4. Crear cuenta — idUsuarioRef es opcional, se vincula después
        Autenticacion user = new Autenticacion();
        user.setIdUsuarioRef(dto.getIdUsuarioRef()); // puede ser null
        user.setUsername(dto.getUsername());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRol(rolSolicitado);

        return mapToDTO(repository.save(user), null);
    }

    public AuthResponseDTO login(String username, String password) {
        Autenticacion user = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en el sistema"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("La contraseña ingresada es incorrecta");
        }

        // Cargamos los datos extra
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("rol", user.getRol());
        extraClaims.put("idUsuarioRef", user.getIdUsuarioRef());

        // generar el token
        String token = jwtService.generarToken(extraClaims, user.getUsername());

        return mapToDTO(user, token);
    }

    // validar token
    public AuthResponseDTO validarToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new RuntimeException("Token no proporcionado");
        }

        // eliminamos el barerd
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            // extraer usuario
            String username = jwtService.extraerUsername(token);

            Autenticacion user = repository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado para el token"));

            // retornamos en caso de fallo
            return mapToDTO(user, token);

        } catch (Exception e) {
            // atrapamos en caso de alguna exception
            throw new RuntimeException("Token inválido o expirado");
        }
    }

    // ResponseDTO
    private AuthResponseDTO mapToDTO(Autenticacion user, String token) {
        return new AuthResponseDTO(
                user.getIdAuth(),
                user.getIdUsuarioRef(),
                user.getUsername(),
                user.getRol(),
                token);
    }

    public AuthResponseDTO vincularUsuario(Long idAuth, Long idUsuarioRef) {
        if (idUsuarioRef == null) {
            throw new RuntimeException("El idUsuarioRef es obligatorio para vincular.");
        }

        Autenticacion user = repository.findById(idAuth)
                .orElseThrow(() -> new RuntimeException(
                        "Cuenta de autenticación no encontrada con ID: " + idAuth));

        // Verificar que el usuario existe en ms-usuarios
        try {
            usuarioClient.verificarExistencia(idUsuarioRef);
        } catch (Exception e) {
            throw new RuntimeException(
                    "El usuario con ID " + idUsuarioRef + " no existe en ms-usuarios.");
        }

        user.setIdUsuarioRef(idUsuarioRef);
        return mapToDTO(repository.save(user), null);
    }
}
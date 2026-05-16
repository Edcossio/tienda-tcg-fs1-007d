package com.tiendatcg.ms_auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tiendatcg.ms_auth.client.UsuarioClient;
import com.tiendatcg.ms_auth.dto.AuthRequestDTO;
import com.tiendatcg.ms_auth.dto.AuthResponseDTO;
import com.tiendatcg.ms_auth.model.Autenticacion;
import com.tiendatcg.ms_auth.repository.AuthRepository;

@Service
@RequiredArgsConstructor
public class AutenticacionService {
    private final AuthRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final UsuarioClient usuarioClient; // <--- Inyectamos el cliente

    public AuthResponseDTO validarExistenciaUsuario(AuthRequestDTO dto) {

        try {
            usuarioClient.verificarExistencia(dto.getIdUsuarioRef());
        } catch (Exception e) {

            throw new RuntimeException("No se puede crear autenticación: El usuario con ID "
                    + dto.getIdUsuarioRef() + " no existe.");
        }

        Autenticacion user = new Autenticacion();
        user.setIdUsuarioRef(dto.getIdUsuarioRef());
        user.setUsername(dto.getUsername());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRol(dto.getRol());

        return mapToDTO(repository.save(user), null);
    }



    public AuthResponseDTO validarToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new RuntimeException("Token no proporcionado");
        }
        // El token que generaste en login tiene formato "TK-rol-idUsuarioRef"
        String[] parts = token.split("-");
        if (parts.length != 3 || !"TK".equals(parts[0])) {
            throw new RuntimeException("Token inválido");
        }
        String rol = parts[1];
        Long idUsuarioRef;
        try {
            idUsuarioRef = Long.parseLong(parts[2]);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Token mal formado");
        }
        Autenticacion user = repository.findByIdUsuarioRef(idUsuarioRef)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado para el token"));
        // Verificar que el rol coincida con el almacenado (opcional)
        if (!user.getRol().equalsIgnoreCase(rol)) {
            throw new RuntimeException("Token corrupto: rol no coincide");
        }
        // Devuelve el DTO con los datos (el token puede ser el mismo o regenerarlo)
        return mapToDTO(user, token);
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

    // REGISTRAR
    public AuthResponseDTO registrar(AuthRequestDTO dto) {

        Autenticacion user = new Autenticacion();
        user.setIdUsuarioRef(dto.getIdUsuarioRef());
        user.setUsername(dto.getUsername());

        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRol(dto.getRol());

        return mapToDTO(repository.save(user), null);
    }

    // LOGIN
    public AuthResponseDTO login(String username, String password) {
        Autenticacion user = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en el sistema")); // 400 Bad Request
                                                                                                 // [cite: 102]

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("La contraseña ingresada es incorrecta");
        }

        String token = "TK-" + user.getRol() + "-" + user.getIdUsuarioRef();

        return mapToDTO(user, token);
    }

}

package com.tcgstore.ms_auth.service;

import com.tcgstore.ms_auth.client.UsuarioClient;
import com.tcgstore.ms_auth.dto.AuthRequestDTO;
import com.tcgstore.ms_auth.dto.AuthResponseDTO;
import com.tcgstore.ms_auth.model.Autenticacion;
import com.tcgstore.ms_auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

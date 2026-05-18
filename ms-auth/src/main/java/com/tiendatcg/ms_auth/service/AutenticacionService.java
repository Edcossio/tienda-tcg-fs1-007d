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
    
    private final AuthRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final UsuarioClient usuarioClient; 
    private final JwtService jwtService; 

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

  
    public AuthResponseDTO registrar(AuthRequestDTO dto) {
        Autenticacion user = new Autenticacion();
        user.setIdUsuarioRef(dto.getIdUsuarioRef());
        user.setUsername(dto.getUsername());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRol(dto.getRol());

        return mapToDTO(repository.save(user), null);
    }

    
    public AuthResponseDTO login(String username, String password) {
        Autenticacion user = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en el sistema")); 

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("La contraseña ingresada es incorrecta");
        }

        //Cargamos los datos extra 
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("rol", user.getRol());
        extraClaims.put("idUsuarioRef", user.getIdUsuarioRef());

        //generar el token 
        String token = jwtService.generarToken(extraClaims, user.getUsername());

        return mapToDTO(user, token);
    }

    // validar token 
    public AuthResponseDTO validarToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new RuntimeException("Token no proporcionado");
        }

        //eliminamos el barer en casa de 
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
}
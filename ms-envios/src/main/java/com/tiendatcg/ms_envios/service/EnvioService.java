package com.tiendatcg.ms_envios.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.tiendatcg.ms_envios.model.Envio;
import com.tiendatcg.ms_envios.repository.EnvioRepository;
import com.tiendatcg.ms_envios.exception.ResourceNotFoundException;
import com.tiendatcg.ms_envios.dto.EnvioRequestDTO;
import com.tiendatcg.ms_envios.dto.EnvioResponseDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnvioService {
    private final EnvioRepository envioRepository;

<<<<<<< HEAD
    public List<EnvioResponseDTO> obtenerTodos() {
=======
    // Método de validación de roles
    private boolean esRolPermitido(String rolUsuario, String... roles) {
        if (rolUsuario == null) return false;
        for (String rol : roles) {
            if (rolUsuario.equalsIgnoreCase(rol)) {
                return true;
            }
        }
        return false;
    }

    public List<EnvioResponseDTO> obtenerTodos(String rol) {
        // Regla: Solo el personal de la tienda puede ver todos los envíos globales
        if (!esRolPermitido(rol, "EMPLEADO", "ADMIN")) {
            throw new RuntimeException("Acceso denegado: no tienes permisos para ver el historial de envíos.");
        }
        
>>>>>>> 8ddf14780ca4f4214dbdf37e3f4980e5a8320e7d
        return envioRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

<<<<<<< HEAD
    public EnvioResponseDTO obtenerPorId(Long id) {
=======
    public EnvioResponseDTO obtenerPorId(Long id, String rol) {
        // Aquí USER, EMPLEADO y ADMIN pueden entrar, pero para USER 
        // a futuro deberías validar que el envío le pertenece (con el idPedido).
        if (!esRolPermitido(rol, "USER", "EMPLEADO", "ADMIN")) {
            throw new RuntimeException("Acceso denegado.");
        }

>>>>>>> 8ddf14780ca4f4214dbdf37e3f4980e5a8320e7d
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Envio no encontrado: " + id));
        return mapToResponseDTO(envio);
    }

<<<<<<< HEAD
    public EnvioResponseDTO crearEnvio(EnvioRequestDTO request) {
=======
    public EnvioResponseDTO crearEnvio(EnvioRequestDTO request, String rol) {
        // Regla: Solo el sistema o un empleado puede generar una guía de envío
        if (!esRolPermitido(rol, "EMPLEADO", "ADMIN")) {
            throw new RuntimeException("Acceso denegado: solo el personal autorizado puede registrar envíos.");
        }

>>>>>>> 8ddf14780ca4f4214dbdf37e3f4980e5a8320e7d
        Envio envio = new Envio();
        envio.setIdPedidoRef(request.getIdPedidoRef());
        envio.setDireccionDestino(request.getDireccionDestino());
        envio.setTransportadora(request.getTransportadora());
        envio.setEstadoEnvio(request.getEstadoEnvio());
<<<<<<< HEAD
=======
        
>>>>>>> 8ddf14780ca4f4214dbdf37e3f4980e5a8320e7d
        return mapToResponseDTO(envioRepository.save(envio));
    }

    private EnvioResponseDTO mapToResponseDTO(Envio envio) {
        EnvioResponseDTO dto = new EnvioResponseDTO();
        dto.setIdEnvio(envio.getIdEnvio());
        dto.setIdPedidoRef(envio.getIdPedidoRef());
        dto.setDireccionDestino(envio.getDireccionDestino());
        dto.setTransportadora(envio.getTransportadora());
        dto.setEstadoEnvio(envio.getEstadoEnvio());
        dto.setFechaCreacion(envio.getFechaCreacion());
        return dto;
    }
}
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

    public List<EnvioResponseDTO> obtenerTodos() {
        return envioRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public EnvioResponseDTO obtenerPorId(Long id) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Envio no encontrado: " + id));
        return mapToResponseDTO(envio);
    }

    public EnvioResponseDTO crearEnvio(EnvioRequestDTO request) {
        Envio envio = new Envio();
        envio.setIdPedidoRef(request.getIdPedidoRef());
        envio.setDireccionDestino(request.getDireccionDestino());
        envio.setTransportadora(request.getTransportadora());
        envio.setEstadoEnvio(request.getEstadoEnvio());
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
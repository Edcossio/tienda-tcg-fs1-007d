package com.tiendatcg.ms_envios.service;

import com.tiendatcg.ms_envios.dto.EnvioRequestDTO;
import com.tiendatcg.ms_envios.dto.EnvioResponseDTO;
import com.tiendatcg.ms_envios.model.Envio;
import com.tiendatcg.ms_envios.repository.EnvioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnvioService {

    private final EnvioRepository envioRepository;

    // Convertir de Entidad a DTO (lo que pide el grupo)
    private EnvioResponseDTO mapToDTO(Envio envio) {
        return EnvioResponseDTO.builder()
                .idEnvio(envio.getIdEnvio())
                .idPedidoRef(envio.getIdPedidoRef())
                .direccionDestino(envio.getDireccionDestino())
                .transportadora(envio.getTransportadora())
                .estadoEnvio(envio.getEstadoEnvio())
                .build();
    }

    public List<EnvioResponseDTO> findAll() {
        return envioRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public EnvioResponseDTO save(EnvioRequestDTO dto) {
        Envio envio = new Envio();
        envio.setIdPedidoRef(dto.getIdPedidoRef());
        envio.setDireccionDestino(dto.getDireccionDestino());
        envio.setTransportadora(dto.getTransportadora());
        envio.setEstadoEnvio("PENDIENTE"); // Estado inicial por defecto

        return mapToDTO(envioRepository.save(envio));
    }
}
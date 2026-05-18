package com.tiendatcg.ms_envios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnvioResponseDTO {
    private Long idEnvio;
    private Long idPedidoRef;
    private String direccionDestino;
    private String transportadora;
    private String estadoEnvio;
}
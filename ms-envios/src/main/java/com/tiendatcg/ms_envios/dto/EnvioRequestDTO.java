package com.tiendatcg.ms_envios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnvioRequestDTO {
    private Long idPedidoRef;
    private String direccionDestino;
    private String transportadora;
}
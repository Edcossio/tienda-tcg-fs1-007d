package com.tiendatcg.ms_notificaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionRequestDTO {
    private Long idUsuario;
    private String mensaje;
    private String tipo;
}
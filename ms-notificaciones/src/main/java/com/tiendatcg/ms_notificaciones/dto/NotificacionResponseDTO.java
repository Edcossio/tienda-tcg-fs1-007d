package com.tiendatcg.ms_notificaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder 
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionResponseDTO {
    private Long idNotificacion;
    private String mensaje;
    private String tipo;
    private LocalDateTime fechaEnvio;
    private boolean leido;
}
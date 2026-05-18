package com.tiendatcg.ms_notificaciones.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data 
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNotificacion;
    
    private Long idUsuario;
    private String mensaje;
    private String tipo;
    private LocalDateTime fechaEnvio;
    private boolean leido;

    @PrePersist
    public void prePersist() {
        this.fechaEnvio = LocalDateTime.now();
        this.leido = false;
    }
}
package com.tiendatcg.ms_resenas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResenaResponseDTO {
    private Long idResena;
    private Long idProducto;
    private Long idUsuario;
    private Integer calificacion;
    private String comentario;
}
package com.tiendatcg.ms_resenas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResenaRequestDTO {
    private Long idProducto;
    private Long idUsuario;
    private Integer calificacion;
    private String comentario;
}
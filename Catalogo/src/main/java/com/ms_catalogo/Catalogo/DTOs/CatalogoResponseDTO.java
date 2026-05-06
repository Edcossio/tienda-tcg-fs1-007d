package com.ms_catalogo.Catalogo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogoResponseDTO {

    private Long id;

    private String nombre;

    private String descripcion;

    private Double precio;

    private Integer stock;

    private String categoria;
}

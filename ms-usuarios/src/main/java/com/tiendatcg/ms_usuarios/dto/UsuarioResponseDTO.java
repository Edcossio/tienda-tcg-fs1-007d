package com.tiendatcg.ms_usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioResponseDTO {

    private Long idPerfil;
    //private Long idAuthRef; descomentar cuando implemente sping security
    private String nombreCompleto;
    private String correoElectronico;
    private String direccionFisica;
    
    // Datos de la tabla Fidelidad
    private Integer totalPuntos;
    private String categoriaVip;
}

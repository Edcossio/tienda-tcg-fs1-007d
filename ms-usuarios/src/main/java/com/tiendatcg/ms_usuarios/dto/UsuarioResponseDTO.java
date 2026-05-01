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

    private Long id;
    private String nombreUsuario;
    private String email;
    private String rol;
}

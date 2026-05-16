package com.tiendatcg.ms_auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDTO {
    @NotNull(message = "El id de usuario es obligatorio")
    private Long idUsuarioRef;

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String username;

    @NotBlank(message = "La clave es obligatoria")
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    private String rol;
}

package com.tiendatcg.ms_usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequestDTO {
    
    @NotBlank(message="El bombre de usuario es obligatorio")
    private String nombreUsuario;

    @NotBlank(message="El corre electronico es obligatorio")
    @Email(message="Formato de correo electronico invalido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contraseña;

    private String rol;
    
}

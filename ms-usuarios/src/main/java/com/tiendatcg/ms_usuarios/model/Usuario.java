package com.tiendatcg.ms_usuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="usuarios")
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Usuario {

    // atributo id

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    @Column(unique = true, nullable = false, length = 30)
    private String nombreUsuario;

    @NotBlank
    @Email
    @Column(unique= true, nullable = false, length = 100)
    private String email;

    @NotBlank
    @Column(nullable = false, length=32)
    private String contraseña;

    @Column(name = "rol", length = 20)
    private String rol;
}

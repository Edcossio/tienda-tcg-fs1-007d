package com.tcgstore.ms_auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "autenticacion")
public class Autenticacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auth")
    private Long idAuth; // #id_auth

    @Column(name = "id_usuario_ref", nullable = false)
    private Long idUsuarioRef; // * id usuario

    @Column(name = "nombre_usuario", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "clave_hash", nullable = false)
    private String password;

    @Column(name = "rol_asignado", nullable = false, length = 20)
    private String rol;

}

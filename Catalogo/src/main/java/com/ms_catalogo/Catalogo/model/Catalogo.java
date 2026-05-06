package com.ms_catalogo.Catalogo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "catalogo")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Catalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotBlank
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @NotBlank
    @Column(name = "precio", nullable = false)
    private Double precio;

    @NotBlank
    @Column(name = "stock")
    private Integer stock;

    @NotBlank
    @Column(name = "categoria")
    private String categoria;
}

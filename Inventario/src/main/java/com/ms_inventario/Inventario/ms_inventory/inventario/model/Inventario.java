package com.ms_inventario.Inventario.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "INVENTARIOS")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_CARTA_REF", nullable = false)
    private Long idCartaRef;

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    @Column(name = "ESTADO_CARTA", nullable = false, length = 50)
    private String estadoCarta;

    @Column(name = "UBICACION", length = 200)
    private String ubicacion;
}
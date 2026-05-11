package com.tcgstore.ms_pagos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcgstore.ms_pagos.model.Pago;

public interface PagoRepository extends JpaRepository<Pago,Long> {

    // para buscar un pago en funcion al pedido 
    Optional<Pago> findByIdPedidoRef(Long idPedidoRef);

}

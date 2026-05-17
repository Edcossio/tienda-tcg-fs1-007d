package com.tiendatcg.ms_pagos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="ms-envios",path="/api/envios")
public interface EnvioClient {
    @GetMapping("/validar-envio/{idPedido}")
    boolean verificarPedidoExiste(@PathVariable("idPedido") Long idPedido);
}

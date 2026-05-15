package com.tcgstore.ms_pagos.client;

import com.tcgstore.ms_pagos.dto.AuthResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-auth", path = "/api/auth")
public interface AuthClient {

    @GetMapping("/validar")
    AuthResponseDTO validarToken(@RequestHeader("Authorization") String token);
}
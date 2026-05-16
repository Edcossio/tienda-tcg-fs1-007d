package com.tiendatcg.ms_usuarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.tiendatcg.ms_usuarios.dto.AuthResponseDTO;

@FeignClient(name = "ms-auth",path = "/api/auth")
public interface AuthClient {
    @GetMapping("/validar-token")
    AuthResponseDTO validarToken(@RequestHeader("Authorization") String token);
}
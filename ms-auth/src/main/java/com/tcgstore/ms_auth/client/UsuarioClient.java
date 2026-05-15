package com.tcgstore.ms_auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-usuarios", path = "/api/usuarios")
public interface UsuarioClient {

    @GetMapping("/{id}")
    Object verificarExistencia(@PathVariable("id") Long id);
    
}

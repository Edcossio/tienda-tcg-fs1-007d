package com.tiendatcg.ms_envios.controller;

import com.tiendatcg.ms_envios.dto.EnvioRequestDTO;
import com.tiendatcg.ms_envios.dto.EnvioResponseDTO;
import com.tiendatcg.ms_envios.service.EnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @GetMapping("/test")
    public String test() {
        return "Microservicio de envios funcionando con arquitectura DTO";
    }

    @PostMapping
    public EnvioResponseDTO crearEnvio(@RequestBody EnvioRequestDTO dto) {
        return envioService.save(dto);
    }

    @GetMapping
    public List<EnvioResponseDTO> listarTodos() {
        return envioService.findAll();
    }
}
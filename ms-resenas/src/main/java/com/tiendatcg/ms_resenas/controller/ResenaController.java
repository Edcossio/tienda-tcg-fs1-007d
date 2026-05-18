package com.tiendatcg.ms_resenas.controller;

import com.tiendatcg.ms_resenas.dto.ResenaRequestDTO;
import com.tiendatcg.ms_resenas.dto.ResenaResponseDTO;
import com.tiendatcg.ms_resenas.service.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @PostMapping
    public ResenaResponseDTO crear(@RequestBody ResenaRequestDTO dto) {
        return resenaService.guardar(dto);
    }

    @GetMapping
    public List<ResenaResponseDTO> listar() {
        return resenaService.obtenerTodas();
    }
}
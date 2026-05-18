package com.tiendatcg.ms_notificaciones.controller;

import com.tiendatcg.ms_notificaciones.dto.NotificacionRequestDTO;
import com.tiendatcg.ms_notificaciones.dto.NotificacionResponseDTO;
import com.tiendatcg.ms_notificaciones.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService service;

    @PostMapping
    public NotificacionResponseDTO crear(@RequestBody NotificacionRequestDTO dto) {
        return service.crear(dto);
    }

    @GetMapping("/usuario/{id}")
    public List<NotificacionResponseDTO> listar(@PathVariable Long id) {
        return service.listarPorUsuario(id);
    }
}
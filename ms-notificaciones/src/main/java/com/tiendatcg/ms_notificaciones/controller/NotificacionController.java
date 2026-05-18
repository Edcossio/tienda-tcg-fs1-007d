package com.tiendatcg.ms_notificaciones.controller;

import com.tiendatcg.ms_notificaciones.model.Notificacion;
import com.tiendatcg.ms_notificaciones.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionRepository repository;

    
    @PostMapping
    public Notificacion crear(@RequestBody Notificacion notificacion) {
        return repository.save(notificacion);
    }

    
    @GetMapping("/usuario/{idUsuario}")
    public List<Notificacion> listarPorUsuario(@PathVariable Long idUsuario) {
        return repository.findByIdUsuarioDestinoOrderByFechaCreacionDesc(idUsuario);
    }

    
    @PutMapping("/{id}/leer")
    public Notificacion marcarComoLeida(@PathVariable Long id) {
        return repository.findById(id).map(notificacion -> {
            notificacion.setLeida(true);
            return repository.save(notificacion);
        }).orElse(null);
    }
}
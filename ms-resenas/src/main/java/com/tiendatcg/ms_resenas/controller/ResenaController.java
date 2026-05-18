package com.tiendatcg.ms_resenas.controller;

import com.tiendatcg.ms_resenas.model.Resena;
import com.tiendatcg.ms_resenas.repository.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired // <-- Esto le dice a Spring que conecte la base de datos automáticamente
    private ResenaRepository repository;

    @GetMapping
    public List<Resena> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Resena crear(@RequestBody Resena resena) {
        return repository.save(resena);
    }

    @GetMapping("/producto/{id}")
    public List<Resena> porProducto(@PathVariable Long id) {
        return repository.findByIdProductoRef(id);
    }
}
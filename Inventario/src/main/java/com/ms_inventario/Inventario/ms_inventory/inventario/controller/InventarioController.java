package com.ms_inventario.Inventario.ms_inventory.inventario.controller;

import com.ms_inventario.Inventario.ms_inventory.inventario.dto.InventarioRequestDTO;
import com.ms_inventario.Inventario.ms_inventory.inventario.dto.InventarioResponseDTO;
import com.ms_inventario.Inventario.ms_inventory.inventario.service.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<List<InventarioResponseDTO>> getAllInventarios() {
        return ResponseEntity.ok(inventarioService.getAllInventarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioResponseDTO> getInventarioById(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.getInventarioById(id));
    }

    @GetMapping("/carta/{idCartaRef}")
    public ResponseEntity<List<InventarioResponseDTO>> getInventariosByCartaRef(@PathVariable Long idCartaRef) {
        return ResponseEntity.ok(inventarioService.getInventariosByCartaRef(idCartaRef));
    }

    @GetMapping("/carta/{idCartaRef}/estado/{estadoCarta}")
    public ResponseEntity<InventarioResponseDTO> getInventarioByCartaRefAndEstado(
            @PathVariable Long idCartaRef,
            @PathVariable String estadoCarta) {
        return ResponseEntity.ok(inventarioService.getInventarioByCartaRefAndEstado(idCartaRef, estadoCarta));
    }

    @PostMapping
    public ResponseEntity<InventarioResponseDTO> createInventario(@Valid @RequestBody InventarioRequestDTO requestDTO) {
        return ResponseEntity.status(201).body(inventarioService.saveInventario(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioResponseDTO> updateInventario(
            @PathVariable Long id,
            @Valid @RequestBody InventarioRequestDTO requestDTO) {
        return ResponseEntity.ok(inventarioService.updateInventario(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventario(@PathVariable Long id) {
        inventarioService.deleteInventario(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock/add")
    public ResponseEntity<InventarioResponseDTO> addStock(
            @PathVariable Long id,
            @RequestParam int cantidad) {
        return ResponseEntity.ok(inventarioService.addStock(id, cantidad));
    }

    @PatchMapping("/{id}/stock/remove")
    public ResponseEntity<InventarioResponseDTO> removeStock(
            @PathVariable Long id,
            @RequestParam int cantidad) {
        return ResponseEntity.ok(inventarioService.subtractStock(id, cantidad));
    }
}
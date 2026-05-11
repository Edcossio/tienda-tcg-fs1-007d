package com.ms_catalogo.Catalogo.Controller;

import com.ms_catalogo.Catalogo.DTOs.CatalogoRequestDTO;
import com.ms_catalogo.Catalogo.DTOs.CatalogoResponseDTO;
import com.ms_catalogo.Catalogo.Service.CatalogoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    /**
     * GET /api/catalogo - Obtener todos los productos
     */
    @GetMapping
    public ResponseEntity<List<CatalogoResponseDTO>> obtenerTodos() {
        try {
            List<CatalogoResponseDTO> catalogo = catalogoService.obtenerTodos();
            return ResponseEntity.ok(catalogo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/catalogo/{id} - Obtener producto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CatalogoResponseDTO> obtenerPorId(@PathVariable Long id) {
        try {
            Optional<CatalogoResponseDTO> catalogo = catalogoService.obtenerPorId(id);
            return catalogo.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /api/catalogo - Crear nuevo producto
     */
    @PostMapping
    public ResponseEntity<CatalogoResponseDTO> crear(@Valid @RequestBody CatalogoRequestDTO requestDTO) {
        try {
            CatalogoResponseDTO catalogo = catalogoService.guardar(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(catalogo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DELETE /api/catalogo/{id} - Eliminar producto
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            catalogoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/catalogo/nombre/{nombre} - Obtener producto por nombre
     */
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<CatalogoResponseDTO> obtenerPorNombre(@PathVariable String nombre) {
        try {
            Optional<CatalogoResponseDTO> catalogo = catalogoService.obtenerPorNombre(nombre);
            return catalogo.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/catalogo/categoria/{categoria} - Obtener productos por categoría
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<CatalogoResponseDTO>> obtenerPorCategoria(@PathVariable String categoria) {
        try {
            List<CatalogoResponseDTO> catalogo = catalogoService.obtenerPorCategoria(categoria);
            return ResponseEntity.ok(catalogo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

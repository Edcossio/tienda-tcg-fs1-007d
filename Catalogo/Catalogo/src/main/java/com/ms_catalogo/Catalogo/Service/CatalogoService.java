package com.ms_catalogo.Catalogo.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ms_catalogo.Catalogo.DTOs.CatalogoRequestDTO;
import com.ms_catalogo.Catalogo.DTOs.CatalogoResponseDTO;
import com.ms_catalogo.Catalogo.Repository.CatalogoRepository;
import com.ms_catalogo.Catalogo.model.Catalogo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CatalogoService {

    private final CatalogoRepository catalogoRepository;

    public List<CatalogoResponseDTO> obtenerTodos() {
        return catalogoRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Optional<CatalogoResponseDTO> obtenerPorId(Long id) {
        return catalogoRepository.findById(id)
                .map(this::mapToResponse);
    }

    public CatalogoResponseDTO guardar(CatalogoRequestDTO request) {
        // Validación de negocio extra:
        if (catalogoRepository.findByNombre(request.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe un producto con el nombre: " + request.getNombre());
        }
        Catalogo catalogo = Catalogo.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .stock(request.getStock() != null ? request.getStock() : 0)
                .categoria(request.getCategoria())
                .build();
        return mapToResponse(catalogoRepository.save(catalogo));
    }

    public void eliminar(Long id) {
        catalogoRepository.deleteById(id);
    }

    public Optional<CatalogoResponseDTO> obtenerPorNombre(String nombre) {
        return catalogoRepository.findByNombre(nombre).map(this::mapToResponse);
    }

    public List<CatalogoResponseDTO> obtenerPorCategoria(String categoria) {
        return catalogoRepository.findByCategoria(categoria).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CatalogoResponseDTO mapToResponse(Catalogo catalogo) {
        return CatalogoResponseDTO.builder()
                .id(catalogo.getId())
                .nombre(catalogo.getNombre())
                .descripcion(catalogo.getDescripcion())
                .precio(catalogo.getPrecio())
                .stock(catalogo.getStock())
                .categoria(catalogo.getCategoria())
                .build();
    }
}

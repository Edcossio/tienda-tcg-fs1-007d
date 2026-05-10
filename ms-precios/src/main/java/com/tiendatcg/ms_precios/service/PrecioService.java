package com.tiendatcg.ms_precios.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tiendatcg.ms_precios.client.CatalogoClient;
import com.tiendatcg.ms_precios.dto.PrecioRequestDTO;
import com.tiendatcg.ms_precios.dto.PrecioResponseDTO;
import com.tiendatcg.ms_precios.model.GeneradorPrecio;
import com.tiendatcg.ms_precios.repository.PrecioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrecioService {

    private final PrecioRepository precioRepository;
    private final CatalogoClient catalogoClient; 
    // private final AuthClient authClient;

    private PrecioResponseDTO mapToDTO(GeneradorPrecio precio) {
        return new PrecioResponseDTO(
                precio.getIdPrecio(),
                precio.getIdCartaRef(),
                precio.getValorMercado(),
                precio.getFechaRegistro()
        );
    }

    public List<PrecioResponseDTO> obtenerTodos() {
        return precioRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<PrecioResponseDTO> obtenerPorId(Long id) {
        return precioRepository.findById(id).map(this::mapToDTO);
    }

    public List<PrecioResponseDTO> obtenerHistorialPorCarta(Long idCartaRef) {
        return precioRepository.findByIdCartaRefOrderByFechaRegistroDesc(idCartaRef)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public PrecioResponseDTO guardar(PrecioRequestDTO dto) {
        
        // COMENTADO TEMPORALMENTE HASTA QUE MS-CATALOGO EXISTA
        /*
        boolean cartaExiste = catalogoClient.verificarCartaExiste(dto.getIdCartaRef());
        if (!cartaExiste) {
            throw new RuntimeException("La carta con ID " + dto.getIdCartaRef() + " no existe en el catálogo.");
        }
        */

        GeneradorPrecio precio = new GeneradorPrecio();
        precio.setIdCartaRef(dto.getIdCartaRef());
        precio.setValorMercado(dto.getValorMercado());
        
        return mapToDTO(precioRepository.save(precio));
    }

    public Optional<PrecioResponseDTO> actualizar(Long id, PrecioRequestDTO dto) {
        return precioRepository.findById(id).map(existente -> {
            existente.setValorMercado(dto.getValorMercado());
            return mapToDTO(precioRepository.save(existente));
        });
    }

    public void eliminar(Long id) {
        precioRepository.deleteById(id);
    }


}

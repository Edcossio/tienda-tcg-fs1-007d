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

    private PrecioResponseDTO mapToDTO(GeneradorPrecio precio) {
        return new PrecioResponseDTO(
                precio.getIdPrecio(),
                precio.getIdCartaRef(),
                precio.getValorMercado(),
                precio.getFechaRegistro());
    }


    private boolean esRolPermitido(String rolUsuario, String... rolesPermitidos) {
        if (rolUsuario == null) return false;
        for (String rol : rolesPermitidos) {
            if (rolUsuario.equalsIgnoreCase(rol)) {
                return true;
            }
        }
        return false;
    }

   
    public List<PrecioResponseDTO> obtenerTodos(String rol) {
        if (!esRolPermitido(rol, "USER", "EMPLEADO", "ADMIN")) {
            throw new RuntimeException("Acceso denegado: no tienes permisos para consultar precios");
        }
        return precioRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<PrecioResponseDTO> obtenerPorId(Long id, String rol) {
        if (!esRolPermitido(rol, "USER", "EMPLEADO", "ADMIN")) {
            throw new RuntimeException("Acceso denegado: no tienes permisos para consultar precios");
        }
        return precioRepository.findById(id).map(this::mapToDTO);
    }

    public List<PrecioResponseDTO> obtenerHistorialPorCarta(Long idCartaRef, String rol) {
        if (!esRolPermitido(rol, "USER", "EMPLEADO", "ADMIN")) {
            throw new RuntimeException("Acceso denegado: no tienes permisos para consultar precios");
        }
        return precioRepository.findByIdCartaRefOrderByFechaRegistroDesc(idCartaRef)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    
    public PrecioResponseDTO guardar(PrecioRequestDTO dto, String rol) {
        if (!esRolPermitido(rol, "EMPLEADO", "ADMIN")) {
            throw new RuntimeException("Acceso denegado: solo empleados o administradores pueden crear precios");
        }

        boolean cartaExiste = catalogoClient.verificarCartaExiste(dto.getIdCartaRef());
        if (!cartaExiste) {
            throw new RuntimeException("La carta con ID " + dto.getIdCartaRef() + " no existe en el catálogo.");
        }

        GeneradorPrecio precio = new GeneradorPrecio();
        precio.setIdCartaRef(dto.getIdCartaRef());
        precio.setValorMercado(dto.getValorMercado());
        return mapToDTO(precioRepository.save(precio));
    }

    public Optional<PrecioResponseDTO> actualizar(Long id, PrecioRequestDTO dto, String rol) {
        if (!esRolPermitido(rol, "EMPLEADO", "ADMIN")) {
            throw new RuntimeException("Acceso denegado: solo empleados o administradores pueden actualizar precios");
        }

        return precioRepository.findById(id).map(existente -> {
            existente.setValorMercado(dto.getValorMercado());
            return mapToDTO(precioRepository.save(existente));
        });
    }

   
    public void eliminar(Long id, String rol) {
        if (!esRolPermitido(rol, "ADMIN")) {
            throw new RuntimeException("Acceso denegado: solo administradores pueden eliminar precios");
        }
        precioRepository.deleteById(id);
    }
}
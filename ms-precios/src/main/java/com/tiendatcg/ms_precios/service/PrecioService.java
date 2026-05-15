package com.tiendatcg.ms_precios.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tiendatcg.ms_precios.client.AuthClient;
import com.tiendatcg.ms_precios.client.CatalogoClient;
import com.tiendatcg.ms_precios.dto.AuthResponseDTO;
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
    private final AuthClient authClient;

    private PrecioResponseDTO mapToDTO(GeneradorPrecio precio) {
        return new PrecioResponseDTO(
                precio.getIdPrecio(),
                precio.getIdCartaRef(),
                precio.getValorMercado(),
                precio.getFechaRegistro());
    }

    private AuthResponseDTO validarToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new RuntimeException("Token no proporcionado");
        }
        try {
            return authClient.validarToken(token);
        } catch (Exception e) {
            throw new RuntimeException("Token inválido o expirado");
        }
    }

    private boolean esRolPermitido(AuthResponseDTO auth, String... rolesPermitidos) {
        for (String rol : rolesPermitidos) {
            if (auth.getRol().equalsIgnoreCase(rol)) {
                return true;
            }
        }
        return false;
    }

    // ========== GET (USER, EMPLEADO, ADMIN) ==========
    public List<PrecioResponseDTO> obtenerTodos(String token) {
        AuthResponseDTO auth = validarToken(token);
        if (!esRolPermitido(auth, "USER", "EMPLEADO", "ADMIN")) {
            throw new RuntimeException("Acceso denegado: no tienes permisos para consultar precios");
        }
        return precioRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<PrecioResponseDTO> obtenerPorId(Long id, String token) {
        AuthResponseDTO auth = validarToken(token);
        if (!esRolPermitido(auth, "USER", "EMPLEADO", "ADMIN")) {
            throw new RuntimeException("Acceso denegado: no tienes permisos para consultar precios");
        }
        return precioRepository.findById(id).map(this::mapToDTO);
    }

    public List<PrecioResponseDTO> obtenerHistorialPorCarta(Long idCartaRef, String token) {
        AuthResponseDTO auth = validarToken(token);
        if (!esRolPermitido(auth, "USER", "EMPLEADO", "ADMIN")) {
            throw new RuntimeException("Acceso denegado: no tienes permisos para consultar precios");
        }
        return precioRepository.findByIdCartaRefOrderByFechaRegistroDesc(idCartaRef)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // ========== POST / PUT (EMPLEADO, ADMIN) ==========
    public PrecioResponseDTO guardar(PrecioRequestDTO dto, String token) {
        AuthResponseDTO auth = validarToken(token);
        if (!esRolPermitido(auth, "EMPLEADO", "ADMIN")) {
            throw new RuntimeException("Acceso denegado: solo empleados o administradores pueden crear precios");
        }

        // Validación con ms-catalogo

        boolean cartaExiste = catalogoClient.verificarCartaExiste(dto.getIdCartaRef());
        if (!cartaExiste) {
            throw new RuntimeException("La carta con ID " + dto.getIdCartaRef() + " no existe en el catálogo.");
        }

        GeneradorPrecio precio = new GeneradorPrecio();
        precio.setIdCartaRef(dto.getIdCartaRef());
        precio.setValorMercado(dto.getValorMercado());
        return mapToDTO(precioRepository.save(precio));
    }

    public Optional<PrecioResponseDTO> actualizar(Long id, PrecioRequestDTO dto, String token) {
        AuthResponseDTO auth = validarToken(token);
        if (!esRolPermitido(auth, "EMPLEADO", "ADMIN")) {
            throw new RuntimeException("Acceso denegado: solo empleados o administradores pueden actualizar precios");
        }

        return precioRepository.findById(id).map(existente -> {
            existente.setValorMercado(dto.getValorMercado());
            return mapToDTO(precioRepository.save(existente));
        });
    }

    // ========== DELETE (solo ADMIN) ==========
    public void eliminar(Long id, String token) {
        AuthResponseDTO auth = validarToken(token);
        if (!esRolPermitido(auth, "ADMIN")) {
            throw new RuntimeException("Acceso denegado: solo administradores pueden eliminar precios");
        }
        precioRepository.deleteById(id);
    }

}

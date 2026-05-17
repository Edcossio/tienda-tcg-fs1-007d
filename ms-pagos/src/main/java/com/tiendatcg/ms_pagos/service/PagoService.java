package com.tiendatcg.ms_pagos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// Eliminamos AuthClient y AuthResponseDTO de los imports
import com.tiendatcg.ms_pagos.client.EnvioClient;
import com.tiendatcg.ms_pagos.dto.PagoRequestDTO;
import com.tiendatcg.ms_pagos.dto.PagoResponseDTO;
import com.tiendatcg.ms_pagos.model.Pago;
import com.tiendatcg.ms_pagos.repository.PagoRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final EnvioClient pedidoClient; // Solo queda el cliente que sí es de negocio

    private PagoResponseDTO mapToDTO(Pago pago) {
        return new PagoResponseDTO(
                pago.getIdPago(),
                pago.getIdPedidoRef(),
                pago.getMontoTotal(),
                pago.getMetodoPago(),
                pago.getEstadoPago(),
                pago.getFechaTransaccion());
    }

    // Solo ADMIN y EMPLEADO ven todos los pagos. Recibimos el "rol" directamente.
    public List<PagoResponseDTO> obtenerTodos(String rol) {
        if ("USER".equals(rol)) {
            throw new RuntimeException("Acceso denegado: No tienes permisos para ver el historial global.");
        }

        return pagoRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // --- REGLA: Un USER solo puede ver SU pago ---
    public Optional<PagoResponseDTO> obtenerPorId(Long id, String rol) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        if ("USER".equals(rol)) {
            // NOTA: Aquí a futuro deberás validar si el pedido asociado a este pago
            // realmente le pertenece al usuario que está haciendo la petición.
        }

        return Optional.of(mapToDTO(pago));
    }

    public PagoResponseDTO procesarPago(PagoRequestDTO dto, String rol) {

        boolean pedidoExiste = pedidoClient.verificarPedidoExiste(dto.getIdPedidoRef());

        if (!pedidoExiste) {
            throw new RuntimeException("El pedido con ID " + dto.getIdPedidoRef() + " no existe.");
        }

        // monto debe ser válido
        if (dto.getMontoTotal() == null || dto.getMontoTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El monto debe ser mayor a cero.");
        }

        // duplicados
        Optional<Pago> pagoExistente = pagoRepository.findByIdPedidoRef(dto.getIdPedidoRef());
        if (pagoExistente.isPresent() && "COMPLETADO".equals(pagoExistente.get().getEstadoPago())) {
            throw new RuntimeException("Este pedido ya se encuentra pagado.");
        }

        // AUTORIZACION
        if ("USER".equals(rol)) {
            // Validación extra si aplica
        }

        Pago pago = new Pago();
        pago.setIdPedidoRef(dto.getIdPedidoRef());
        pago.setMontoTotal(dto.getMontoTotal());
        pago.setMetodoPago(dto.getMetodoPago());
        // Estado inicial
        pago.setEstadoPago("COMPLETADO");

        return mapToDTO(pagoRepository.save(pago));
    }

    // Los empleados gestionan devoluciones o cancelaciones
    public PagoResponseDTO anularPago(Long idPago, String rol) {
        if (!"ADMIN".equals(rol) && !"EMPLEADO".equals(rol)) {
            throw new RuntimeException("Solo el personal de la tienda puede anular pagos.");
        }

        Pago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        pago.setEstadoPago("ANULADO");
        return mapToDTO(pagoRepository.save(pago));
    }

    public Optional<PagoResponseDTO> obtenerPorPedido(Long idPedidoRef, String rol) {
        return pagoRepository.findByIdPedidoRef(idPedidoRef).map(pago -> {
            if ("USER".equals(rol)) {
                // Validación extra si aplica
            }
            return mapToDTO(pago);
        });
    }
}
package com.tiendatcg.ms_pagos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.tiendatcg.ms_pagos.client.AuthClient;
import com.tiendatcg.ms_pagos.client.PedidoClient;
import com.tiendatcg.ms_pagos.dto.AuthResponseDTO;
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
    private final AuthClient authClient;
    private final PedidoClient pedidoClient;

    private PagoResponseDTO mapToDTO(Pago pago) {
        return new PagoResponseDTO(
                pago.getIdPago(),
                pago.getIdPedidoRef(),
                pago.getMontoTotal(),
                pago.getMetodoPago(),
                pago.getEstadoPago(),
                pago.getFechaTransaccion());
    }

    // Solo ADMIN y EMPLEADO ven todos los pagos
    public List<PagoResponseDTO> obtenerTodos(String token) {
        AuthResponseDTO auth = authClient.validarToken(token);

        if ("USER".equals(auth.getRol())) {
            throw new RuntimeException("Acceso denegado: No tienes permisos para ver el historial global.");
        }

        return pagoRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // --- REGLA: Un USER solo puede ver SU pago ---
    public Optional<PagoResponseDTO> obtenerPorId(Long id, String token) {
        AuthResponseDTO auth = authClient.validarToken(token);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        if ("USER".equals(auth.getRol())) {

        }

        return Optional.of(mapToDTO(pago));
    }

    public PagoResponseDTO procesarPago(PagoRequestDTO dto, String token) {

        boolean pedidoExiste = pedidoClient.verificarPedidoExiste(dto.getIdPedidoRef());

        if (!pedidoExiste) {

            throw new RuntimeException("El pedido con ID " + dto.getIdPedidoRef() + " no existe.");

        }

        AuthResponseDTO auth = authClient.validarToken(token);

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
        if ("USER".equals(auth.getRol())) {

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
    public PagoResponseDTO anularPago(Long idPago, String token) {
        AuthResponseDTO auth = authClient.validarToken(token);

        if (!"ADMIN".equals(auth.getRol()) && !"EMPLEADO".equals(auth.getRol())) {
            throw new RuntimeException("Solo el personal de la tienda puede anular pagos.");
        }

        Pago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        pago.setEstadoPago("ANULADO");
        return mapToDTO(pagoRepository.save(pago));
    }

    public Optional<PagoResponseDTO> obtenerPorPedido(Long idPedidoRef, String token) {
        
        AuthResponseDTO auth = authClient.validarToken(token);

        
        return pagoRepository.findByIdPedidoRef(idPedidoRef).map(pago -> {
            
            if ("USER".equals(auth.getRol())) {
                
            }
            return mapToDTO(pago);
        });
    }
}
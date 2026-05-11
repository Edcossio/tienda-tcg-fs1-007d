package com.tcgstore.ms_pagos.service;

// import com.tcgstore.ms_pagos.client.PedidoClient;
import com.tcgstore.ms_pagos.dto.PagoRequestDTO;
import com.tcgstore.ms_pagos.dto.PagoResponseDTO;
import com.tcgstore.ms_pagos.model.Pago;
import com.tcgstore.ms_pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    // private final PedidoClient pedidoClient;

    private PagoResponseDTO mapToDTO(Pago pago) {
        return new PagoResponseDTO(
                pago.getIdPago(),
                pago.getIdPedidoRef(),
                pago.getMontoTotal(),
                pago.getMetodoPago(),
                pago.getEstadoPago(),
                pago.getFechaTransaccion()
        );
    }

    public List<PagoResponseDTO> obtenerTodos() {
        return pagoRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<PagoResponseDTO> obtenerPorId(Long id) {
        return pagoRepository.findById(id).map(this::mapToDTO);
    }

    public Optional<PagoResponseDTO> obtenerPorPedido(Long idPedidoRef) {
        return pagoRepository.findByIdPedidoRef(idPedidoRef).map(this::mapToDTO);
    }

    public PagoResponseDTO procesarPago(PagoRequestDTO dto) {
        
        // COMENTADO HASTA QUE MS-PEDIDOS EXISTA
        /*
        boolean pedidoExiste = pedidoClient.verificarPedidoExiste(dto.getIdPedidoRef());
        if (!pedidoExiste) {
            throw new RuntimeException("El pedido con ID " + dto.getIdPedidoRef() + " no existe.");
        }
        */

        // Verificamos si ya existe un pago para este pedido para evitar cobros dobles
        Optional<Pago> pagoExistente = pagoRepository.findByIdPedidoRef(dto.getIdPedidoRef());
        if (pagoExistente.isPresent() && "COMPLETADO".equals(pagoExistente.get().getEstadoPago())) {
            throw new RuntimeException("Este pedido ya se encuentra pagado.");
        }

        Pago pago = new Pago();
        pago.setIdPedidoRef(dto.getIdPedidoRef());
        pago.setMontoTotal(dto.getMontoTotal());
        pago.setMetodoPago(dto.getMetodoPago());
        
        // Simulamos que la pasarela de pago aprueba la transacción inmediatamente
        pago.setEstadoPago("COMPLETADO"); 
        
        return mapToDTO(pagoRepository.save(pago));
    }
}
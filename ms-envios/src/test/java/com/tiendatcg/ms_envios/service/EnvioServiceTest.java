package com.tiendatcg.ms_envios.service;

import com.tiendatcg.ms_envios.dto.EnvioRequestDTO;
import com.tiendatcg.ms_envios.dto.EnvioResponseDTO;
import com.tiendatcg.ms_envios.model.Envio;
import com.tiendatcg.ms_envios.repository.EnvioRepository;
import com.tiendatcg.ms_envios.client.PagoClient;
import com.tiendatcg.ms_envios.exception.AccesoDenegado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository;

    @Mock
    private PagoClient pagoClient;

    @InjectMocks
    private EnvioService envioService;

    private EnvioRequestDTO requestDTO;
    private Envio envio;

    @BeforeEach
    void setUp() {
        requestDTO = new EnvioRequestDTO();
        requestDTO.setIdPedidoRef(200L);
        requestDTO.setDireccionDestino("Calle Falsa 123");
        requestDTO.setTransportadora("Starken");
        requestDTO.setEstadoEnvio("PREPARANDO");

        envio = new Envio();
        envio.setIdEnvio(1L);
        envio.setIdPedidoRef(200L);
        envio.setDireccionDestino("Calle Falsa 123");
        envio.setTransportadora("Starken");
        envio.setEstadoEnvio("PREPARANDO");
        envio.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    void crearEnvioExitoTest() {
        // GIVEN: Simulamos validación exitosa en ms-pagos y guardado en BD
        when(pagoClient.obtenerEstadoPago(200L)).thenReturn(null); // Simulamos que no lanza error
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);

        // WHEN: Ejecutamos el método con rol permitido
        EnvioResponseDTO response = envioService.crearEnvio(requestDTO, "ADMIN");

        // THEN: Validamos
        assertNotNull(response);
        assertEquals("Starken", response.getTransportadora());
        assertEquals("Calle Falsa 123", response.getDireccionDestino());
        
        verify(pagoClient, times(1)).obtenerEstadoPago(200L);
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    @Test
    void crearEnvioAccesoDenegadoTest() {
        // Ejecutamos el método con un rol NO permitido (USER) y verificamos que lance la excepción
        assertThrows(AccesoDenegado.class, () -> {
            envioService.crearEnvio(requestDTO, "USER");
        });

        // Verificamos que si se denegó el acceso, JAMÁS se llamó a la base de datos ni al cliente
        verify(pagoClient, times(0)).obtenerEstadoPago(any());
        verify(envioRepository, times(0)).save(any(Envio.class));
    }

    @Test
    void eliminarEnvioTest() {
        // GIVEN:
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));

        // WHEN: Ejecutamos como ADMIN
        envioService.eliminarEnvio(1L, "ADMIN");

        // THEN:
        verify(envioRepository, times(1)).findById(1L);
        verify(envioRepository, times(1)).delete(envio);
    }
}
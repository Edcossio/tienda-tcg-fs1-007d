package com.tiendatcg.ms_notificaciones.service;

import com.tiendatcg.ms_notificaciones.dto.NotificacionRequestDTO;
import com.tiendatcg.ms_notificaciones.dto.NotificacionResponseDTO;
import com.tiendatcg.ms_notificaciones.model.Notificacion;
import com.tiendatcg.ms_notificaciones.repository.NotificacionRepository;
import com.tiendatcg.ms_notificaciones.client.UsuarioClient;
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
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository repository;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private NotificacionService notificacionService;

    private NotificacionRequestDTO requestDTO;
    private Notificacion notificacion;

    @BeforeEach
    void setUp() {
        // 1. Preparamos el DTO de entrada simulando la petición del usuario
        requestDTO = new NotificacionRequestDTO();
        requestDTO.setIdUsuarioDestino(50L);
        requestDTO.setTitulo("Alerta de Seguridad");
        requestDTO.setMensaje("Se detectó un inicio de sesión inusual.");

        // 2. Preparamos el objeto Notificacion simulando la base de datos
        notificacion = new Notificacion();
        notificacion.setIdNotificacion(1L);
        notificacion.setIdUsuarioDestino(50L);
        notificacion.setTitulo("Alerta de Seguridad");
        notificacion.setMensaje("Se detectó un inicio de sesión inusual.");
        notificacion.setLeida(false);
        notificacion.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    void crearTest() {
        // GIVEN: Le decimos a Mockito qué hacer cuando el servicio llame a la BD
        when(repository.save(any(Notificacion.class))).thenReturn(notificacion);

        // WHEN: Ejecutamos el método real
        NotificacionResponseDTO response = notificacionService.crear(requestDTO);

        // THEN: Validamos la respuesta
        assertNotNull(response);
        assertEquals(1L, response.getIdNotificacion());
        assertEquals("Alerta de Seguridad", response.getTitulo());
        assertFalse(response.getLeida()); // Validamos regla de negocio: se crea como no leída
        
        // Verificamos comunicación con Feign y guardado en BD
        verify(usuarioClient, times(1)).obtenerUsuarioPorId(50L);
        verify(repository, times(1)).save(any(Notificacion.class));
    }

    @Test
    void eliminarTest() {
        // GIVEN: Simulamos que la notificación sí existe
        when(repository.findById(1L)).thenReturn(Optional.of(notificacion));

        // WHEN: Ejecutamos el método eliminar
        notificacionService.eliminar(1L);

        // THEN: Verificamos que buscó el ID y luego lo eliminó
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(notificacion);
    }
}
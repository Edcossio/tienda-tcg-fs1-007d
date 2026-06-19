package com.tiendatcg.ms_resenas.service;

import com.tiendatcg.ms_resenas.dto.ResenaRequestDTO;
import com.tiendatcg.ms_resenas.dto.ResenaResponseDTO;
import com.tiendatcg.ms_resenas.model.Resena;
import com.tiendatcg.ms_resenas.repository.ResenaRepository;
import com.tiendatcg.ms_resenas.client.UsuarioClient;
import com.tiendatcg.ms_resenas.client.ProductoClient;
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

@ExtendWith(MockitoExtension.class)
class ResenaServiceTest {

    @Mock
    private ResenaRepository repository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private ProductoClient productoClient;

    @InjectMocks
    private ResenaService resenaService;

    private ResenaRequestDTO requestDTO;
    private Resena resena;

    @BeforeEach
    void setUp() {
        // 1. Preparamos los datos de entrada (Lo que enviaría el usuario)
        requestDTO = new ResenaRequestDTO();
        requestDTO.setIdUsuarioRef(50L);
        requestDTO.setIdProductoRef(100L);
        requestDTO.setComentario("Excelente producto");
        requestDTO.setCalificacion(5);

        // 2. Preparamos lo que simulará guardar la base de datos
        resena = new Resena();
        resena.setIdResena(1L);
        resena.setIdUsuarioRef(50L);
        resena.setIdProductoRef(100L);
        resena.setComentario("Excelente producto");
        resena.setCalificacion(5);
        resena.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    void crearTest() {
        // GIVEN: Le decimos a Mockito qué hacer cuando el servicio llame a la base de datos
        when(repository.save(any(Resena.class))).thenReturn(resena);

        // WHEN: Ejecutamos el método real de tu servicio
        ResenaResponseDTO response = resenaService.crear(requestDTO);

        // THEN: Validamos que la respuesta sea correcta
        assertNotNull(response);
        assertEquals(1L, response.getIdResena());
        assertEquals("Excelente producto", response.getComentario());
        
        // Verificamos que se haya consultado a los otros microservicios (Feign) y guardado en BD
        verify(usuarioClient, times(1)).obtenerUsuarioPorId(50L);
        verify(productoClient, times(1)).obtenerProductoPorId(100L);
        verify(repository, times(1)).save(any(Resena.class));
    }

    @Test
    void eliminarTest() {
        // GIVEN: Simulamos que la reseña sí existe en la base de datos
        when(repository.findById(1L)).thenReturn(Optional.of(resena));

        // WHEN: Ejecutamos el método eliminar
        resenaService.eliminar(1L);

        // THEN: Verificamos que se buscó por ID y luego se borró
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(resena);
    }
}
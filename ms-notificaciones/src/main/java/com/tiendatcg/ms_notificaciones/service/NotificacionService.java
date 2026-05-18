package com.tiendatcg.ms_notificaciones.service;

import com.tiendatcg.ms_notificaciones.dto.NotificacionRequestDTO;
import com.tiendatcg.ms_notificaciones.dto.NotificacionResponseDTO;
import com.tiendatcg.ms_notificaciones.model.Notificacion;
import com.tiendatcg.ms_notificaciones.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    private NotificacionResponseDTO mapToDTO(Notificacion n) {
        return NotificacionResponseDTO.builder()
                .idNotificacion(n.getIdNotificacion())
                .mensaje(n.getMensaje())
                .tipo(n.getTipo())
                .fechaEnvio(n.getFechaEnvio())
                .leido(n.isLeido())
                .build();
    }

    public NotificacionResponseDTO crear(NotificacionRequestDTO dto) {
        Notificacion n = new Notificacion();
        n.setIdUsuario(dto.getIdUsuario());
        n.setMensaje(dto.getMensaje());
        n.setTipo(dto.getTipo());
        return mapToDTO(notificacionRepository.save(n));
    }

    public List<NotificacionResponseDTO> listarPorUsuario(Long idUsuario) {
        return notificacionRepository.findByIdUsuario(idUsuario).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
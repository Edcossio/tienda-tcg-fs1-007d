package com.tiendatcg.ms_resenas.service;

import com.tiendatcg.ms_resenas.dto.ResenaRequestDTO;
import com.tiendatcg.ms_resenas.dto.ResenaResponseDTO;
import com.tiendatcg.ms_resenas.model.Resena;
import com.tiendatcg.ms_resenas.repository.ResenaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResenaService {

    private final ResenaRepository resenaRepository;

    private ResenaResponseDTO mapToDTO(Resena resena) {
        return ResenaResponseDTO.builder()
                .idResena(resena.getIdResena())
                .idProducto(resena.getIdProducto())
                .idUsuario(resena.getIdUsuario())
                .calificacion(resena.getCalificacion())
                .comentario(resena.getComentario())
                .build();
    }

    public ResenaResponseDTO guardar(ResenaRequestDTO dto) {
        Resena resena = new Resena();
        resena.setIdProducto(dto.getIdProducto());
        resena.setIdUsuario(dto.getIdUsuario());
        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());
        
        return mapToDTO(resenaRepository.save(resena));
    }

    public List<ResenaResponseDTO> obtenerTodas() {
        return resenaRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
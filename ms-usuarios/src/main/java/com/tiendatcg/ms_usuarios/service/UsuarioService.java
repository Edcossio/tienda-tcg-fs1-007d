package com.tiendatcg.ms_usuarios.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tiendatcg.ms_usuarios.dto.UsuarioRequestDTO;
import com.tiendatcg.ms_usuarios.dto.UsuarioResponseDTO;
import com.tiendatcg.ms_usuarios.model.Fidelidad;
import com.tiendatcg.ms_usuarios.model.Usuario;
import com.tiendatcg.ms_usuarios.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    // private final AuthClient authClient; // Inyectamos FeignClient

    private UsuarioResponseDTO mapToDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getIdPerfil(),
                // usuario.getIdAuthRef(),
                usuario.getNombreCompleto(),
                usuario.getCorreoElectronico(),
                usuario.getDireccionFisica(),
                usuario.getPuntos() != null ? usuario.getPuntos().getTotalPuntos() : 0,
                usuario.getPuntos() != null ? usuario.getPuntos().getCategoriaVip() : "N/A"
        );
    }

    public List<UsuarioResponseDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<UsuarioResponseDTO> obtenerPorId(Long id) {
        return usuarioRepository.findById(id).map(this::mapToDTO);
    }

    public UsuarioResponseDTO guardar(UsuarioRequestDTO dto) {
        // 1. Validar que el correo no exista
        if (usuarioRepository.findByEmail(dto.getCorreoElectronico()) != null) {
            throw new RuntimeException("El correo electrónico ya está en uso.");
        }

        // 2. Comunicarse con MS-Auth vía OpenFeign (Opcional si MS-Auth está apagado por ahora, puedes comentar esto)
        /*
        boolean authExiste = authClient.verificarUsuarioExiste(dto.getIdAuthRef());
        if (!authExiste) {
            throw new RuntimeException("El ID de Autenticación proporcionado no existe en MS-Auth.");
        }
        */

        // 3. Crear Perfil
        Usuario usuario = new Usuario();
        // usuario.setIdAuthRef(dto.getIdAuthRef());
        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setCorreoElectronico(dto.getCorreoElectronico());
        usuario.setDireccionFisica(dto.getDireccionFisica());

        // 4. Crear su tarjeta de puntos inicial (1:1)
        Fidelidad tarjetaFideliad = new Fidelidad();
        tarjetaFideliad.setUsuario(usuario);
        tarjetaFideliad.setTotalPuntos(0);
        tarjetaFideliad.setCategoriaVip("NUEVO");

        usuario.setPuntos(tarjetaFideliad);

        return mapToDTO(usuarioRepository.save(usuario));
    }

    public Optional<UsuarioResponseDTO> actualizar(Long id, UsuarioRequestDTO dto) {
        return usuarioRepository.findById(id).map(existente -> {
            existente.setNombreCompleto(dto.getNombreCompleto());
            // No actualizamos idAuthRef ni correo para evitar inconsistencias graves de identidad
            existente.setDireccionFisica(dto.getDireccionFisica());
            return mapToDTO(usuarioRepository.save(existente));
        });
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}

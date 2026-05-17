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

    private UsuarioResponseDTO mapToDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .idPerfil(usuario.getIdPerfil())
                .nombreCompleto(usuario.getNombreCompleto())
                .correoElectronico(usuario.getCorreoElectronico())
                .direccionFisica(usuario.getDireccionFisica())
                .totalPuntos(usuario.getPuntos() != null ? usuario.getPuntos().getTotalPuntos() : 0)
                .categoriaVip(usuario.getPuntos() != null ? usuario.getPuntos().getCategoriaVip() : "N/A")
                .build();
    }

    private boolean esRolPermitido(String rolUsuario, String... roles) {
        if (rolUsuario == null) return false;
        for (String rol : roles) {
            if (rolUsuario.equalsIgnoreCase(rol)) {
                return true;
            }
        }
        return false;
    }

    // ========== GET ==========
    public List<UsuarioResponseDTO> obtenerTodos(String rol) {
        // Solo EMPLEADO o ADMIN pueden ver todos los usuarios
        if (!esRolPermitido(rol, "EMPLEADO", "ADMIN")) {
            throw new RuntimeException("Acceso denegado: solo empleados o administradores pueden listar todos los usuarios");
        }
        return usuarioRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<UsuarioResponseDTO> obtenerPorId(Long id, String rol, Long idUsuarioLogueado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // USER solo puede ver su propio perfil
        if (esRolPermitido(rol, "USER")) {
            if (!idUsuarioLogueado.equals(usuario.getIdPerfil())) {
                throw new RuntimeException("Acceso denegado: no puedes ver el perfil de otro usuario");
            }
        } else if (!esRolPermitido(rol, "EMPLEADO", "ADMIN")) {
            throw new RuntimeException("Acceso denegado: rol no autorizado");
        }
        return Optional.of(mapToDTO(usuario));
    }

    // ========== POST (crear) - solo EMPLEADO o ADMIN ==========
    public UsuarioResponseDTO guardar(UsuarioRequestDTO dto, String rol) {
        if (!esRolPermitido(rol, "EMPLEADO", "ADMIN")) {
            throw new RuntimeException("Acceso denegado: solo empleados o administradores pueden crear usuarios");
        }

        // Validar correo único
        if (usuarioRepository.findByCorreoElectronico(dto.getCorreoElectronico()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya está en uso");
        }

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setCorreoElectronico(dto.getCorreoElectronico());
        usuario.setDireccionFisica(dto.getDireccionFisica());

        Fidelidad tarjeta = new Fidelidad();
        tarjeta.setUsuario(usuario);
        tarjeta.setTotalPuntos(0);
        tarjeta.setCategoriaVip("NUEVO");
        usuario.setPuntos(tarjeta);

        return mapToDTO(usuarioRepository.save(usuario));
    }

    // ========== PUT (actualizar) ==========
    public Optional<UsuarioResponseDTO> actualizar(Long id, UsuarioRequestDTO dto, String rol, Long idUsuarioLogueado) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // USER solo puede actualizar su propio perfil y solo ciertos campos
        if (esRolPermitido(rol, "USER")) {
            if (!idUsuarioLogueado.equals(existente.getIdPerfil())) {
                throw new RuntimeException("Acceso denegado: no puedes actualizar otro usuario");
            }
            // USER solo puede modificar nombreCompleto y direccionFisica
            existente.setNombreCompleto(dto.getNombreCompleto());
            existente.setDireccionFisica(dto.getDireccionFisica());
            // No puede cambiar correo
        } else if (esRolPermitido(rol, "EMPLEADO", "ADMIN")) {
            // EMPLEADO/ADMIN pueden modificar todo excepto quizás el correo
            existente.setNombreCompleto(dto.getNombreCompleto());
            existente.setDireccionFisica(dto.getDireccionFisica());
            // Opcional: permitir cambio de correo solo a ADMIN
            if (dto.getCorreoElectronico() != null && !dto.getCorreoElectronico().equals(existente.getCorreoElectronico())) {
                if (esRolPermitido(rol, "ADMIN")) {
                    existente.setCorreoElectronico(dto.getCorreoElectronico());
                } else {
                    throw new RuntimeException("Solo administradores pueden cambiar el correo electrónico");
                }
            }
        } else {
            throw new RuntimeException("Acceso denegado: rol no autorizado");
        }

        return Optional.of(mapToDTO(usuarioRepository.save(existente)));
    }

    // ========== DELETE - solo ADMIN ==========
    public void eliminar(Long id, String rol) {
        if (!esRolPermitido(rol, "ADMIN")) {
            throw new RuntimeException("Acceso denegado: solo administradores pueden eliminar usuarios");
        }
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}
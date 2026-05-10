package com.tiendatcg.ms_usuarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tiendatcg.ms_usuarios.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long>{

    Optional<Usuario> findByEmail(String correoElectronico);
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
}

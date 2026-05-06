package com.ms_catalogo.Catalogo.Repository;

import com.ms_catalogo.Catalogo.model.Catalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, Long> {

    Optional<Catalogo> findByNombre(String nombre);

    List<Catalogo> findByCategoria(String categoria);
}

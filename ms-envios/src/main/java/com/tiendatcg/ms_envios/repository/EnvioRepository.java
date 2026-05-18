package com.tiendatcg.ms_envios.repository;

import com.tiendatcg.ms_envios.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    // JpaRepository ya trae el save() y el findAll() por defecto
}
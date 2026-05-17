package com.tiendatcg.ms_precios.config;

import com.tiendatcg.ms_precios.model.GeneradorPrecio;
import com.tiendatcg.ms_precios.repository.PrecioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * ═══════════════════════════════════════════════════
 * CLASE: DataInitializer.java (ms-precios)
 * Carga inicial de precios de mercado para el catálogo TCG.
 * ═══════════════════════════════════════════════════
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PrecioRepository precioRepository;

    @Override
    public void run(String... args) throws Exception {

        long totalPrecios = precioRepository.count();

        
        if (totalPrecios > 0) {
            log.info(">>> DataInitializer (ms-precios): La BD ya tiene {} precios registrados. Omitiendo carga.",
                    totalPrecios);
            return;
        }

        log.info(">>> DataInitializer (ms-precios): BD vacía. Inyectando precios base para las cartas...");

       

        // Carta 1: Carta Ultra Rara (Ej. Charizard 1ra Edición) - Alto valor
        precioRepository.save(new GeneradorPrecio(null, 1L, new BigDecimal("150.00"), null));

        // Carta 2: Carta Común (Ej. Pikachu Básico) - Bajo valor
        precioRepository.save(new GeneradorPrecio(null, 2L, new BigDecimal("5.50"), null));

        // Carta 3: Carta Competitiva Meta (Ej. Mewtwo EX) - Valor medio
        precioRepository.save(new GeneradorPrecio(null, 3L, new BigDecimal("45.99"), null));

        // Carta 4: Carta Clásica (Ej. Dragón Blanco de Ojos Azules) - Valor alto
        precioRepository.save(new GeneradorPrecio(null, 4L, new BigDecimal("85.00"), null));

        log.info(">>> DataInitializer (ms-precios): Carga completada. {} precios fijados en el motor.",
                precioRepository.count());
    }
}
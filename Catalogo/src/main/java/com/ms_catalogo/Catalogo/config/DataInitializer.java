package com.ms_catalogo.Catalogo.config;

import com.ms_catalogo.Catalogo.Repository.CatalogoRepository;
import com.ms_catalogo.Catalogo.model.Catalogo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CatalogoRepository catalogoRepository;

    @Override
    public void run(String... args) throws Exception {

        long totalCartas = catalogoRepository.count();

        if (totalCartas > 0) {
            log.info(">>> DataInitializer (ms-catalogo): La BD ya tiene {} cartas. Omitiendo carga.", totalCartas);
            return;
        }

        log.info(">>> DataInitializer (ms-catalogo): BD vacía. Inyectando cartas base al catálogo...");

        // ID Esperado: 1
        Catalogo carta1 = new Catalogo(null, "Charizard 1ra Edición",
                "Carta holográfica icónica, extremadamente rara y muy buscada por coleccionistas del set base.",
                150.00, 5, "Pokémon TCG - Base Set", null, null);

        // ID Esperado: 2
        Catalogo carta2 = new Catalogo(null, "Pikachu Básico",
                "Carta común del ratón eléctrico, ideal para iniciar cualquier mazo.",
                5.50, 150, "Pokémon TCG - Común", null, null);

        // ID Esperado: 3
        Catalogo carta3 = new Catalogo(null, "Mewtwo EX",
                "Poderoso atacante psíquico. Una de las cartas más fuertes del meta actual competitivo.",
                45.99, 25, "Pokémon TCG - EX", null, null);

        // ID Esperado: 4
        Catalogo carta4 = new Catalogo(null, "Dragón Blanco de Ojos Azules",
                "Legendario dragón de destrucción. Nadie puede resistirse a su abrumador poder.",
                85.00, 12, "Yu-Gi-Oh! - Ultra Rare", null, null);

        catalogoRepository.saveAll(List.of(carta1, carta2, carta3, carta4));

        log.info(">>> DataInitializer (ms-catalogo): Carga completada. {} cartas disponibles en vitrina.",
                catalogoRepository.count());
    }
}

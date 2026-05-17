package com.ms_shopingcart.config;

import com.ms_shopingcart.model.Carrito;
import com.ms_shopingcart.model.ItemCarrito;
import com.ms_shopingcart.repository.CarritoRepository; // Ajusta a tu paquete real
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CarritoRepository carritoRepository;

    @Override
    public void run(String... args) throws Exception {

        long totalCarritos = carritoRepository.count();

        
        if (totalCarritos > 0) {
            log.info(">>> DataInitializer (ms-shopping-cart): La BD ya tiene {} carritos. Omitiendo carga.",
                    totalCarritos);
            return;
        }

        log.info(">>> DataInitializer (ms-shopping-cart): BD vacía. Armando carritos de prueba...");

        // ── CARRITO 1: Eduardo (Usuario 3) - Carrito Activo ────────

        Carrito carritoEduardo = Carrito.builder()
                .usuarioId(3L)
                .build();

        // 2. Creamos los items y los asociamos al carrito
        ItemCarrito item1 = ItemCarrito.builder()
                .cartaId(1L) // Charizard
                .cantidad(1)
                .precioUnitario(150.00)
                .carrito(carritoEduardo) // 
                .build();
        item1.calcularSubtotal(); // Llama al método que creaste en tu modelo para setear el subtotal

        ItemCarrito item2 = ItemCarrito.builder()
                .cartaId(2L) // Pikachu
                .cantidad(2)
                .precioUnitario(5.50)
                .carrito(carritoEduardo)
                .build();
        item2.calcularSubtotal();

        // 3. Metemos los items a la lista del carrito
        carritoEduardo.getItems().add(item1);
        carritoEduardo.getItems().add(item2);

        // ── CARRITO 2: Otro usuario (Usuario 1) - Carrito abandonado ────────

        Carrito carritoAbandonado = Carrito.builder()
                .usuarioId(1L)
                .estado("ABANDONADO") // Sobrescribimos el estado por defecto
                .build();

        ItemCarrito item3 = ItemCarrito.builder()
                .cartaId(4L) // Dragón Blanco
                .cantidad(1)
                .precioUnitario(85.00)
                .carrito(carritoAbandonado)
                .build();
        item3.calcularSubtotal();

        carritoAbandonado.getItems().add(item3);

        // Al guardar los carritos, JPA guardará automáticamente los Items asociados
        // gracias a cascade = CascadeType.ALL
        carritoRepository.saveAll(List.of(carritoEduardo, carritoAbandonado));

        log.info(">>> DataInitializer (ms-shopping-cart): Carga completada. {} carritos guardados.",
                carritoRepository.count());
    }
}
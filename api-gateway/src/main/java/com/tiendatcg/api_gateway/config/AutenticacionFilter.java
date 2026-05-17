package com.tiendatcg.api_gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AutenticacionFilter implements GlobalFilter {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. Obtener la ruta a la que quiere ir el cliente
        String path = exchange.getRequest().getURI().getPath();

        // 2. Definir las rutas PÚBLICAS (Ajusta según tus endpoints reales)
        if (path.contains("/api/auth/login") || path.contains("/api/auth/registrar")) {
            return chain.filter(exchange); // Dejar pasar sin pedir token
        }

        // 3. Verificar si la petición trae el header "Authorization"
        if (!exchange.getRequest().getHeaders().containsHeader(HttpHeaders.AUTHORIZATION)) {
            return denegarAcceso(exchange);
        }

        // 4. Extraer el token
        String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader = authHeader.substring(7); // Quitar "Bearer "
        } else {
            return denegarAcceso(exchange);
        }

       try {
            String rol = jwtUtil.extraerRol(authHeader);
            String idUsuario = jwtUtil.extraerIdUsuario(authHeader);

            // 6. MUTAR LA PETICIÓN: Preparamos la petición para agregarle los headers
            var requestBuilder = exchange.getRequest().mutate()
                    .header("X-User-Rol", rol); // Agregamos el rol

            // Solo agregamos el header del ID si realmente venía en el token
            if (idUsuario != null) {
                requestBuilder.header("X-User-Id", idUsuario); 
            }

            org.springframework.http.server.reactive.ServerHttpRequest modifiedRequest = requestBuilder.build();

            org.springframework.web.server.ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(modifiedRequest)
                    .build();

            // 7. Si todo está bien, la petición viaja al microservicio con los headers incluidos
            return chain.filter(modifiedExchange);

        } catch (Exception e) {
            System.out.println("Token inválido o expirado: " + e.getMessage());
            return denegarAcceso(exchange);
        }
    }

    // Método de ayuda para devolver Error 401 (No Autorizado)
    private Mono<Void> denegarAcceso(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
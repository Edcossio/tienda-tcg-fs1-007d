package com.tiendatcg.ms_auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Importación clave
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain; // Importación clave

@Configuration
@EnableWebSecurity
public class AuthConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 1. Desactivamos CSRF porque las APIs REST con JWT son "stateless" (sin
                // estado)
                .csrf(csrf -> csrf.disable())

                // 2. Configuramos qué rutas necesitan permiso y cuáles no
                .authorizeHttpRequests(auth -> auth
                        // Le decimos que todo lo que esté bajo /api/auth/** sea PÚBLICO (Login y
                        // Registro)
                        .requestMatchers("/api/auth/**").permitAll()
                        // Cualquier otra petición interna (si la hubiera) requerirá autenticación
                        .anyRequest().authenticated())

                // 3. Forzamos a que la sesión sea STATELESS (sin cookies de sesión)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .build();
    }
}
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
                // Desactivamos CSRF 
                .csrf(csrf -> csrf.disable())

                // configuracion de rutas
                .authorizeHttpRequests(auth -> auth
                        // volvemos la ruta /api/auth/** publica 
                        .requestMatchers("/api/auth/**").permitAll()
                        // con anyrequest() hacemos que cualquier otra ruta tenga auth 
                        .anyRequest().authenticated())

                // forzamos a que la sesion no tenga cookies(tuve un error que me pedia token para el login)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .build();
    }
}
package com.tiendatcg.ms_auth.config;

import com.tiendatcg.ms_auth.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                // Rutas públicas — única fuente de verdad
                                                .requestMatchers(
                                                                "/api/auth/login",
                                                                "/api/auth/registrar")
                                                .permitAll()
                                                // Rutas internas entre microservicios
                                                .requestMatchers(
                                                                "/api/auth/validar-token",
                                                                "/api/auth/extraer-claims")
                                                .permitAll()
                                                // Rutas protegidas por rol
                                                .requestMatchers("/api/auth/vincular/**")
                                                .hasAnyRole("ADMIN", "EMPLEADO")
                                                // Todo lo demás requiere autenticación
                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
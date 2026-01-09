package com.bmt.HolaMundo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                    // Permitir acceso público a la ruta raíz
                    .requestMatchers("/").permitAll()
                    // Todas las demás rutas también son públicas
                    .anyRequest().permitAll()
                )
                // Deshabilitar el formulario de login por defecto
                .formLogin(form -> form.disable())
                // Deshabilitar el logout por defecto
                .logout(logout -> logout.disable())
                .build();
    }
}
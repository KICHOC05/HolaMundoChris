package com.bmt.HolaMundo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                    // Permitir acceso público a todas las rutas
                    .anyRequest().permitAll()
                )
                
                // Configurar CSRF manualmente
                .csrf(csrf -> csrf
                    .csrfTokenRepository(csrfTokenRepository())
                    // Permitir solicitudes GET sin CSRF
                    .ignoringRequestMatchers(
                        "/api/files/**",    // Excluir rutas de API de archivos
                        "/h2-console/**"    // Excluir H2 Console
                    )
                )
                
                // Configurar para H2 Console
                .headers(headers -> headers
                    .frameOptions(frame -> frame.disable())
                )
                
                // Deshabilitar formularios predeterminados
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable())
                
                .build();
    }
    
    // Configurar el repositorio de tokens CSRF
    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-CSRF-TOKEN");
        repository.setParameterName("_csrf");
        return repository;
    }
}
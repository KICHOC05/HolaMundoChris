package com.bmt.HolaMundo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // =====================================================
            // 🔐 AUTORIZACIÓN
            // =====================================================
            .authorizeHttpRequests(auth -> auth

                // 🌍 Rutas públicas generales
                .requestMatchers(
                        "/",
                        "/login",
                        "/register",
                        "/error/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/h2-console/**"
                ).permitAll()

                // 🌍 LISTADO PÚBLICO DE USUARIOS
                .requestMatchers("/users", "/users/search").permitAll()

                // 🔐 CRUD protegido solo ADMIN
                .requestMatchers(
                        "/users/create",
                        "/users/save",
                        "/users/edit/**",
                        "/users/update/**",
                        "/users/delete/**"
                ).hasRole("ADMIN")

                // 🔐 Cualquier otra requiere autenticación
                .anyRequest().authenticated()
            )

            // =====================================================
            // 🔑 LOGIN PERSONALIZADO
            // =====================================================
            .formLogin(form -> form
                    .loginPage("/login")
                    .defaultSuccessUrl("/users", true)
                    .permitAll()
            )

            // =====================================================
            // 🚪 LOGOUT
            // =====================================================
            .logout(logout -> logout
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
            )

            // =====================================================
            // 🛡 CSRF
            // =====================================================
            .csrf(csrf -> csrf
                    .csrfTokenRepository(
                            CookieCsrfTokenRepository.withHttpOnlyFalse()
                    )
                    .ignoringRequestMatchers("/h2-console/**")
            )

            // =====================================================
            // 🗄 H2 Console
            // =====================================================
            .headers(headers ->
                    headers.frameOptions(frame -> frame.disable())
            );

        return http.build();
    }

    // 🔐 Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
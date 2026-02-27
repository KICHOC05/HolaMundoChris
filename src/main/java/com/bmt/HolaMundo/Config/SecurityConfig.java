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
            // 🔓 TODO PERMITIDO
            // =====================================================
            .authorizeHttpRequests(auth -> auth
                    .anyRequest().permitAll()
            )

            // =====================================================
            // 🔑 LOGIN (opcional, pero ya no obligatorio)
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
                    .logoutSuccessUrl("/")
                    .permitAll()
            )

            // =====================================================
            // 🛡 CSRF ACTIVO (recomendado mantenerlo)
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

    // 🔐 Password Encoder (lo dejamos porque tu proyecto lo usa)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
package com.ufide.biblioapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // Ignorar CSRF para la API y para el login de Postman
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    "/api/**",
                    "/procesar-login"
                )
            )

            .authorizeHttpRequests(auth -> auth

                // Paginas publicas
                .requestMatchers(
                    "/libros",
                    "/libros/**",
                    "/login",
                    "/procesar-login",
                    "/403",
                    "/css/**",
                    "/js/**"
                ).permitAll()

                // API publica solamente para consultas GET
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/libros",
                    "/api/libros/**"
                ).permitAll()

                // Todo lo demas requiere autenticacion
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                // Pagina que muestra el formulario
                .loginPage("/login")

                // URL que procesa username y password
                .loginProcessingUrl("/procesar-login")

                // Si el login es correcto
                .defaultSuccessUrl("/libros", true)

                // Si usuario o password son incorrectos
                .failureUrl("/login?error")

                .permitAll()
            )

            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )

            .exceptionHandling(exception -> exception
                .accessDeniedPage("/403")
            );

        return http.build();
    }
}
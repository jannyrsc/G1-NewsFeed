package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Configuração de segurança para o sistema.
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    SecurityFilter securityFilter;
    //Configuração do filtro de segurança para diferentes URLs e métodos HTTP.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/noticias").permitAll()
                        .requestMatchers(HttpMethod.GET, "/noticias/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categorias/all").permitAll() // Endpoint público para listar categorias
                        .requestMatchers(HttpMethod.POST, "/categorias").authenticated() // Endpoint protegido para adicionar categorias
                        .requestMatchers(HttpMethod.GET, "/users/{userId}").authenticated()
                        .requestMatchers(HttpMethod.POST, "/noticias/{userId}/categorias").authenticated()
                        .requestMatchers(HttpMethod.POST, "/user/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    //Configura o gerenciador de autenticação para o sistema.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    // Configura o codificador de senhas usado pelo sistema (BCrypt).
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

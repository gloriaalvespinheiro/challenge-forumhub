package com.forumhub.apitopicos.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    // 1. Permitir acesso a endpoints que não exigem autenticação
                    req.requestMatchers(HttpMethod.POST, "/login").permitAll();
                    req.requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/usuarios").permitAll(); // Cadastro de usuário
                    // 2. Exigir autenticação para os GETs de usuário
                    // Estas regras devem vir ANTES de anyRequest().authenticated()
                    req.requestMatchers(HttpMethod.GET, "/usuarios/**").authenticated();
                    req.requestMatchers(HttpMethod.GET, "/usuarios").authenticated();// Detalhamento de usuário
                    req.requestMatchers(HttpMethod.PUT, "/usuarios/**").authenticated(); // Atualização de utilizador
                    req.requestMatchers(HttpMethod.DELETE, "/usuarios/**").authenticated(); // Exclusão de utilizador

                    req.requestMatchers(HttpMethod.POST, "/respostas").authenticated(); // Cadastro de resposta
                    req.requestMatchers(HttpMethod.GET, "/respostas/**").authenticated(); // Detalhamento de resposta
                    req.requestMatchers(HttpMethod.GET, "/respostas").authenticated();
                    req.requestMatchers(HttpMethod.PUT, "/respostas/**").authenticated(); // Atualização de resposta
                    req.requestMatchers(HttpMethod.DELETE, "/respostas/**").authenticated(); // Exclusão de resposta


                    // 4. Todas as outras requisições exigem autenticação
                    req.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



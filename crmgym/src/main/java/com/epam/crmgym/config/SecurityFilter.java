package com.epam.crmgym.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityFilter {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessionMangConfig -> sessionMangConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/authentication/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/trainees/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/trainers/register").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/trainees/**").authenticated()
                        .requestMatchers( "/api/trainers/**").authenticated()
                        .requestMatchers("/api/trainings/**").authenticated()
                        .requestMatchers("/api/training-types/**").authenticated()
                        .requestMatchers("/api/users/**").authenticated()
                        .anyRequest().denyAll()
                ).csrf(csrf -> csrf.disable());

        return http.build();
    }
}

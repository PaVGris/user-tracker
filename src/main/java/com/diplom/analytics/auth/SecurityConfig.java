package com.diplom.analytics.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Для трекерных запросов
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "X-Client-ID", "Content-Type"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/tracker/**", configuration);
        source.registerCorsConfiguration("/tracker.js", configuration);

        // Для API
        CorsConfiguration apiConfig = new CorsConfiguration();
        apiConfig.setAllowedOrigins(List.of("http://localhost:3001", "http://localhost:3000")); // Исправлено - каждая строка отдельно
        apiConfig.setAllowedMethods(List.of("*"));
        apiConfig.setAllowedHeaders(List.of("*"));
        apiConfig.setAllowCredentials(true);
        source.registerCorsConfiguration("/data/**", apiConfig);

        // Для аутентификации
        CorsConfiguration authConfig = new CorsConfiguration();
        authConfig.setAllowedOrigins(List.of("http://localhost:3001", "http://localhost:3000"));
        authConfig.setAllowedMethods(List.of("POST", "OPTIONS"));
        authConfig.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        authConfig.setAllowCredentials(true);
        source.registerCorsConfiguration("/auth/**", authConfig);

        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/tracker/**").permitAll()
                        .requestMatchers("/tracker.js").permitAll()
                        .requestMatchers(HttpMethod.GET, "/data/heatmap/*/*").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
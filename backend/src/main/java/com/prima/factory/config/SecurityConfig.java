package com.prima.factory.config;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prima.factory.dto.ApiResponse;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain chain(HttpSecurity http, ObjectMapper objectMapper) throws Exception {
        return http
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/v1/auth/**", "/ws/**"))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/v1/auth/**", "/actuator/health", "/swagger-ui/**", "/v3/api-docs/**", "/ws/**")
                .permitAll()
                .anyRequest().authenticated())
            .exceptionHandling(errors -> errors
                .authenticationEntryPoint((request, response, exception) ->
                    writeError(response, objectMapper, HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다."))
                .accessDeniedHandler((request, response, exception) ->
                    writeError(response, objectMapper, HttpServletResponse.SC_FORBIDDEN, "접근 권한이 없습니다.")))
            .build();
    }

    private static void writeError(
        HttpServletResponse response, ObjectMapper objectMapper, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getOutputStream(), ApiResponse.error(message));
    }
}

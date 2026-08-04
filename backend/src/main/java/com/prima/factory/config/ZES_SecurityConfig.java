package com.prima.factory.config;

import java.io.IOException;
import java.util.Set;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prima.factory.dto.ZES_ApiResponse;

@Configuration
public class ZES_SecurityConfig
{
    private static final Set<String> ZES_CSRF_IGNORED_POST_PATHS = Set.of(
        "/api/v1/auth/login",
        "/api/v1/auth/signup",
        "/api/v1/auth/logout");

    private static final RequestMatcher ZES_CSRF_IGNORED_REQUESTS = ZES_request ->
    {
        String ZES_path = ZES_request.getServletPath();
        return (HttpMethod.POST.matches(ZES_request.getMethod())
            && ZES_CSRF_IGNORED_POST_PATHS.contains(ZES_path))
            || ZES_path.equals("/ws")
            || ZES_path.startsWith("/ws/");
    };

    @Bean
    PasswordEncoder ZES_passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain ZES_chain(HttpSecurity ZES_http, ObjectMapper ZES_objectMapper) throws Exception
    {
        return ZES_http
            .csrf(ZES_csrf -> ZES_csrf.ignoringRequestMatchers(ZES_CSRF_IGNORED_REQUESTS))
            .authorizeHttpRequests(ZES_authorization -> ZES_authorization
                .requestMatchers(
                    "/api/v1/auth/**", "/actuator/health", "/swagger-ui/**", "/v3/api-docs/**", "/ws/**")
                .permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .exceptionHandling(ZES_errors -> ZES_errors
                .authenticationEntryPoint((ZES_request, ZES_response, ZES_exception) ->
                    ZES_writeError(ZES_response, ZES_objectMapper,
                        HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다."))
                .accessDeniedHandler((ZES_request, ZES_response, ZES_exception) ->
                    ZES_writeError(ZES_response, ZES_objectMapper,
                        HttpServletResponse.SC_FORBIDDEN, "접근 권한이 없습니다.")))
            .build();
    }

    private static void ZES_writeError(
        HttpServletResponse ZES_response,
        ObjectMapper ZES_objectMapper,
        int ZES_status,
        String ZES_message) throws IOException
    {
        ZES_response.setStatus(ZES_status);
        ZES_response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ZES_response.setCharacterEncoding("UTF-8");
        ZES_objectMapper.writeValue(
            ZES_response.getOutputStream(), ZES_ApiResponse.ZES_error(ZES_message));
    }
}

package com.prima.factory.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.prima.factory.dto.ApiResponse;
import com.prima.factory.mapper.UserMapper;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserMapper users;
    private final PasswordEncoder encoder;

    public AuthController(UserMapper users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @PostMapping("/login")
    ApiResponse<?> login(@RequestBody Map<String, String> body, HttpSession session) {
        var user = users.find(body.get("username"));
        if (user == null || !encoder.matches(
            body.getOrDefault("password", ""), String.valueOf(user.get("passwordHash")))) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        if (!"APPROVED".equals(user.get("approvalStatus")) || Boolean.TRUE.equals(user.get("isLocked"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "승인되지 않았거나 잠긴 계정입니다.");
        }

        var authentication = UsernamePasswordAuthenticationToken.authenticated(
            user.get("username"), null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        var securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        session.setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        session.setAttribute("USER_ID", user.get("id"));

        return ApiResponse.ok(Map.of(
            "id", user.get("id"),
            "username", user.get("username"),
            "name", user.get("fullName")));
    }

    @PostMapping("/logout")
    ApiResponse<?> logout(HttpSession session) {
        SecurityContextHolder.clearContext();
        session.invalidate();
        return ApiResponse.ok(Map.of());
    }

    @GetMapping("/me")
    ApiResponse<?> me(HttpSession session) {
        if (session.getAttribute("USER_ID") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return ApiResponse.ok(Map.of("id", session.getAttribute("USER_ID")));
    }

    @PostMapping("/signup")
    ApiResponse<?> signup(@RequestBody Map<String, Object> body) {
        var copy = new HashMap<>(body);
        copy.put("passwordHash", encoder.encode(String.valueOf(body.get("password"))));
        users.signup(copy);
        return ApiResponse.ok(Map.of("status", "PENDING"));
    }
}

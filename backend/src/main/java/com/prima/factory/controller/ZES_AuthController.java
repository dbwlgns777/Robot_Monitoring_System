package com.prima.factory.controller;

import java.util.Map;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prima.factory.dto.ApiResponse;
import com.prima.factory.dto.ZES_LoginRequest;
import com.prima.factory.dto.ZES_SignupRequest;
import com.prima.factory.service.ZES_AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class ZES_AuthController
{
    private final ZES_AuthService ZES_authService;

    public ZES_AuthController(ZES_AuthService ZES_authService)
    {
        this.ZES_authService = ZES_authService;
    }

    @PostMapping("/login")
    ApiResponse<?> ZES_login(@Valid @RequestBody ZES_LoginRequest ZES_request, HttpSession ZES_session)
    {
        return ApiResponse.ok(ZES_authService.ZES_login(ZES_request, ZES_session));
    }

    @PostMapping("/logout")
    ApiResponse<?> ZES_logout(HttpSession ZES_session)
    {
        ZES_authService.ZES_logout(ZES_session);
        return ApiResponse.ok(Map.of());
    }

    @GetMapping("/me")
    ApiResponse<?> ZES_me(HttpSession ZES_session)
    {
        return ApiResponse.ok(ZES_authService.ZES_currentUser(ZES_session));
    }

    @PostMapping("/signup")
    ApiResponse<?> ZES_signup(@Valid @RequestBody ZES_SignupRequest ZES_request)
    {
        return ApiResponse.ok(ZES_authService.ZES_signup(ZES_request));
    }
}

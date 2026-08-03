package com.prima.factory.controller;

import java.util.Map;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.web.csrf.CsrfToken;

import com.prima.factory.dto.ZES_ApiResponse;
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
    ZES_ApiResponse<?> ZES_login(@Valid @RequestBody ZES_LoginRequest ZES_request, HttpSession ZES_session)
    {
        return ZES_ApiResponse.ZES_ok(ZES_authService.ZES_login(ZES_request, ZES_session));
    }

    @PostMapping("/logout")
    ZES_ApiResponse<?> ZES_logout(HttpSession ZES_session)
    {
        ZES_authService.ZES_logout(ZES_session);
        return ZES_ApiResponse.ZES_ok(Map.of());
    }

    @GetMapping("/me")
    ZES_ApiResponse<?> ZES_me(HttpSession ZES_session)
    {
        return ZES_ApiResponse.ZES_ok(ZES_authService.ZES_currentUser(ZES_session));
    }

    @PostMapping("/signup")
    ZES_ApiResponse<?> ZES_signup(@Valid @RequestBody ZES_SignupRequest ZES_request)
    {
        return ZES_ApiResponse.ZES_ok(ZES_authService.ZES_signup(ZES_request));
    }

    @GetMapping("/csrf")
    ZES_ApiResponse<?> ZES_csrf(CsrfToken ZES_csrfToken)
    {
        return ZES_ApiResponse.ZES_ok(Map.of(
            "token", ZES_csrfToken.getToken(),
            "headerName", ZES_csrfToken.getHeaderName()));
    }
}

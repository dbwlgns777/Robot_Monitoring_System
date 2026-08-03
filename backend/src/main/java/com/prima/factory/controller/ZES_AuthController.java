package com.prima.factory.controller;

import java.util.Map;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import java.time.Duration;

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
    ZES_ApiResponse<?> ZES_login(
        @Valid @RequestBody ZES_LoginRequest ZES_request,
        HttpSession ZES_session,
        HttpServletRequest ZES_httpRequest,
        HttpServletResponse ZES_response)
    {
        Object ZES_user = ZES_authService.ZES_login(ZES_request, ZES_session);
        if (ZES_request.ZES_rememberMe())
        {
            int ZES_rememberSeconds = Math.toIntExact(Duration.ofDays(30).getSeconds());
            ZES_session.setMaxInactiveInterval(ZES_rememberSeconds);
            ResponseCookie ZES_cookie = ResponseCookie.from("JSESSIONID", ZES_session.getId())
                .httpOnly(true)
                .secure(ZES_httpRequest.isSecure())
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofDays(30))
                .build();
            ZES_response.addHeader(HttpHeaders.SET_COOKIE, ZES_cookie.toString());
        }
        return ZES_ApiResponse.ZES_ok(ZES_user);
    }

    @PostMapping("/logout")
    ZES_ApiResponse<?> ZES_logout(HttpSession ZES_session, HttpServletResponse ZES_response)
    {
        ZES_authService.ZES_logout(ZES_session);
        ResponseCookie ZES_cookie = ResponseCookie.from("JSESSIONID", "")
            .httpOnly(true).sameSite("Lax").path("/").maxAge(Duration.ZERO).build();
        ZES_response.addHeader(HttpHeaders.SET_COOKIE, ZES_cookie.toString());
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

package com.prima.factory.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.server.ResponseStatusException;

import com.prima.factory.dto.ZES_LoginRequest;
import com.prima.factory.dto.ZES_SignupRequest;
import com.prima.factory.mapper.ZES_UserMapper;
import com.prima.factory.service.ZES_AuthService;

class ZES_AuthControllerTest
{
    @AfterEach
    void ZES_clearContext()
    {
        SecurityContextHolder.clearContext();
    }

    @Test
    void ZES_approvedUserCanLogin()
    {
        ZES_UserMapper ZES_users = mock(ZES_UserMapper.class);
        var ZES_encoder = new BCryptPasswordEncoder();
        when(ZES_users.ZES_findByUsername("admin")).thenReturn(Map.of(
            "id", 1L, "username", "admin", "fullName", "개발 관리자",
            "passwordHash", ZES_encoder.encode("password"), "approvalStatus", "APPROVED",
            "isLocked", false, "isActive", true));

        var ZES_session = new MockHttpSession();
        var ZES_httpResponse = new MockHttpServletResponse();
        var ZES_response = new ZES_AuthController(new ZES_AuthService(ZES_users, ZES_encoder))
            .ZES_login(new ZES_LoginRequest("admin", "password", true), ZES_session,
                new MockHttpServletRequest(), ZES_httpResponse);

        assertTrue(ZES_response.ZES_success());
        assertTrue(ZES_httpResponse.getHeader("Set-Cookie").contains("Max-Age=2592000"));
        assertEquals(1L, ZES_session.getAttribute("USER_ID"));
        var ZES_context = (SecurityContext) ZES_session.getAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        assertEquals("admin", ZES_context.getAuthentication().getPrincipal());
    }

    @Test
    void ZES_existingSessionRestoresCompleteUser()
    {
        ZES_UserMapper ZES_users = mock(ZES_UserMapper.class);
        when(ZES_users.ZES_findById(1L)).thenReturn(Map.of(
            "id", 1L, "username", "admin", "fullName", "개발 관리자",
            "approvalStatus", "APPROVED", "isLocked", false, "isActive", true));
        when(ZES_users.ZES_findRoleCodes(1L)).thenReturn(java.util.List.of("ROLE_ADMIN"));
        MockHttpSession ZES_session = new MockHttpSession();
        ZES_session.setAttribute("USER_ID", 1L);

        Map<String, Object> ZES_user = new ZES_AuthService(
            ZES_users, new BCryptPasswordEncoder()).ZES_currentUser(ZES_session);

        assertEquals("admin", ZES_user.get("username"));
        assertEquals("개발 관리자", ZES_user.get("name"));
        assertEquals(java.util.List.of("ROLE_ADMIN"), ZES_user.get("roles"));
    }

    @Test
    void ZES_signupPersistsPendingRegistration()
    {
        ZES_UserMapper ZES_users = mock(ZES_UserMapper.class);
        when(ZES_users.ZES_findFactoryId("화성 제1공장")).thenReturn(1L);
        when(ZES_users.ZES_insertRegistration(anyMap())).thenReturn(1);
        var ZES_controller = new ZES_AuthController(
            new ZES_AuthService(ZES_users, new BCryptPasswordEncoder()));

        var ZES_response = ZES_controller.ZES_signup(new ZES_SignupRequest(
            "newuser", "신규 사용자", "password", "password", "화성 제1공장",
            "생산관리팀", "과장", "010-0000-0000", "new@company.com", "생산관리자"));

        assertTrue(ZES_response.ZES_success());
        verify(ZES_users).ZES_insertRegistration(anyMap());
    }

    @Test
    void ZES_signupRejectsMismatchedPasswords()
    {
        var ZES_controller = new ZES_AuthController(new ZES_AuthService(
            mock(ZES_UserMapper.class), new BCryptPasswordEncoder()));

        ResponseStatusException ZES_exception = assertThrows(ResponseStatusException.class,
            () -> ZES_controller.ZES_signup(new ZES_SignupRequest(
                "newuser", "신규 사용자", "password", "different", "화성 제1공장",
                null, null, null, "new@company.com", "생산관리자")));

        assertEquals(HttpStatus.BAD_REQUEST, ZES_exception.getStatusCode());
    }
}

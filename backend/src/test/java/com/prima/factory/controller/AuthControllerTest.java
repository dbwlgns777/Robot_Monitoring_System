package com.prima.factory.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.prima.factory.mapper.UserMapper;

class AuthControllerTest {

    @Test
    void approvedUserCanLoginWithMappedPasswordHash() {
        var users = mock(UserMapper.class);
        var encoder = new BCryptPasswordEncoder();
        when(users.find("admin")).thenReturn(Map.of(
            "id", 1L,
            "username", "admin",
            "fullName", "개발 관리자",
            "passwordHash", encoder.encode("password"),
            "approvalStatus", "APPROVED",
            "isLocked", false));

        var session = new MockHttpSession();
        var response = new AuthController(users, encoder)
            .login(Map.of("username", "admin", "password", "password"), session);

        assertTrue(response.success());
        assertEquals(1L, session.getAttribute("USER_ID"));
    }
}

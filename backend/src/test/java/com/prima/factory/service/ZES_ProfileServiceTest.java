package com.prima.factory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.prima.factory.dto.ZES_PasswordChangeRequest;
import com.prima.factory.mapper.ZES_UserMapper;

class ZES_ProfileServiceTest
{
    @Test
    void ZES_changePasswordVerifiesCurrentPasswordAndStoresBcryptHash()
    {
        ZES_UserMapper ZES_users = mock(ZES_UserMapper.class);
        var ZES_encoder = new BCryptPasswordEncoder();
        when(ZES_users.ZES_findPasswordHash(7L)).thenReturn(ZES_encoder.encode("old-password"));
        when(ZES_users.ZES_updatePassword(
            org.mockito.ArgumentMatchers.eq(7L), org.mockito.ArgumentMatchers.anyString()))
            .thenReturn(1);
        MockHttpSession ZES_session = new MockHttpSession();
        ZES_session.setAttribute("USER_ID", 7L);

        new ZES_ProfileService(ZES_users, ZES_encoder).ZES_changePassword(
            new ZES_PasswordChangeRequest("old-password", "new-password", "new-password"),
            ZES_session);

        verify(ZES_users).ZES_updatePassword(
            org.mockito.ArgumentMatchers.eq(7L),
            argThat(ZES_hash -> ZES_encoder.matches("new-password", ZES_hash)));
    }

    @Test
    void ZES_changePasswordRejectsIncorrectCurrentPassword()
    {
        ZES_UserMapper ZES_users = mock(ZES_UserMapper.class);
        var ZES_encoder = new BCryptPasswordEncoder();
        when(ZES_users.ZES_findPasswordHash(7L)).thenReturn(ZES_encoder.encode("old-password"));
        MockHttpSession ZES_session = new MockHttpSession();
        ZES_session.setAttribute("USER_ID", 7L);

        ResponseStatusException ZES_exception = assertThrows(ResponseStatusException.class,
            () -> new ZES_ProfileService(ZES_users, ZES_encoder).ZES_changePassword(
                new ZES_PasswordChangeRequest("wrong-password", "new-password", "new-password"),
                ZES_session));

        assertEquals(HttpStatus.BAD_REQUEST, ZES_exception.getStatusCode());
    }
}

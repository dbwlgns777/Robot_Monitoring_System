package com.prima.factory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.server.ResponseStatusException;

import com.prima.factory.mapper.ZES_UserMapper;

class ZES_RegistrationApprovalServiceTest
{
    @Test
    void ZES_adminApprovalCreatesUserRoleAndReviewRecord()
    {
        ZES_UserMapper ZES_users = mock(ZES_UserMapper.class);
        when(ZES_users.ZES_findRegistrationForUpdate(10L)).thenReturn(Map.of(
            "id", 10L,
            "username", "worker1",
            "fullName", "작업자",
            "passwordHash", "bcrypt-hash",
            "requestedRole", "생산관리자",
            "status", "PENDING"));
        when(ZES_users.ZES_findRoleIdByName("생산관리자")).thenReturn(2L);
        doAnswer(ZES_invocation ->
        {
            Map<String, Object> ZES_user = ZES_invocation.getArgument(0);
            ZES_user.put("ZES_userId", 20L);
            return 1;
        }).when(ZES_users).ZES_insertApprovedUser(anyMap());
        when(ZES_users.ZES_updateRegistrationStatus(10L, "APPROVED", 1L)).thenReturn(1);

        MockHttpSession ZES_session = ZES_adminSession();
        Map<String, Object> ZES_result =
            new ZES_RegistrationApprovalService(ZES_users).ZES_approve(10L, ZES_session);

        assertEquals("APPROVED", ZES_result.get("status"));
        assertEquals(20L, ZES_result.get("userId"));
        verify(ZES_users).ZES_insertUserRole(20L, 2L);
        verify(ZES_users).ZES_updateRegistrationStatus(10L, "APPROVED", 1L);
    }

    @Test
    void ZES_nonAdminCannotReadPendingRegistrations()
    {
        MockHttpSession ZES_session = new MockHttpSession();
        ZES_session.setAttribute("USER_ID", 2L);
        ZES_session.setAttribute("USER_ROLES", List.of("ROLE_MANAGER"));

        ResponseStatusException ZES_exception = assertThrows(ResponseStatusException.class,
            () -> new ZES_RegistrationApprovalService(mock(ZES_UserMapper.class))
                .ZES_pendingRegistrations(ZES_session));

        assertEquals(HttpStatus.FORBIDDEN, ZES_exception.getStatusCode());
    }

    private MockHttpSession ZES_adminSession()
    {
        MockHttpSession ZES_session = new MockHttpSession();
        ZES_session.setAttribute("USER_ID", 1L);
        ZES_session.setAttribute("USER_ROLES", List.of("ROLE_ADMIN"));
        return ZES_session;
    }
}

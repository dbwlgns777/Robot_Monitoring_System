package com.prima.factory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import com.prima.factory.dto.ZES_RegistrationApprovalRequest;
import com.prima.factory.mapper.ZES_UserMapper;

class ZES_UserAdministrationServiceTest
{
    @Test
    void ZES_adminCanReplaceAnApprovedUsersRole()
    {
        ZES_UserMapper ZES_users = mock(ZES_UserMapper.class);
        when(ZES_users.ZES_countUsersById(20L)).thenReturn(1);
        when(ZES_users.ZES_findRoleIdByCode("ROLE_MANAGER")).thenReturn(2L);
        MockHttpSession ZES_session = new MockHttpSession();
        ZES_session.setAttribute("USER_ID", 1L);
        ZES_session.setAttribute("USER_ROLES", List.of("ROLE_ADMIN"));

        Map<String, Object> ZES_result = new ZES_UserAdministrationService(
            ZES_users, new ZES_AdminAccessService()).ZES_updateRole(
                20L, new ZES_RegistrationApprovalRequest("ROLE_MANAGER"), ZES_session);

        assertEquals("ROLE_MANAGER", ZES_result.get("roleCode"));
        verify(ZES_users).ZES_deleteUserRoles(20L);
        verify(ZES_users).ZES_insertUserRole(20L, 2L);
    }
}

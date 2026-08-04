package com.prima.factory.service;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.prima.factory.dto.ZES_RegistrationApprovalRequest;
import com.prima.factory.mapper.ZES_UserMapper;

@Service
public class ZES_UserAdministrationService
{
    private final ZES_UserMapper ZES_users;
    private final ZES_AdminAccessService ZES_adminAccess;

    public ZES_UserAdministrationService(
        ZES_UserMapper ZES_users, ZES_AdminAccessService ZES_adminAccess)
    {
        this.ZES_users = ZES_users;
        this.ZES_adminAccess = ZES_adminAccess;
    }

    public List<Map<String, Object>> ZES_users(HttpSession ZES_session)
    {
        ZES_adminAccess.ZES_requireAdmin(ZES_session);
        return ZES_users.ZES_findAllUsers();
    }

    public List<Map<String, Object>> ZES_roles(HttpSession ZES_session)
    {
        ZES_adminAccess.ZES_requireAdmin(ZES_session);
        return ZES_users.ZES_findAssignableRoles();
    }

    @Transactional
    public Map<String, Object> ZES_updateRole(
        long ZES_userId,
        ZES_RegistrationApprovalRequest ZES_request,
        HttpSession ZES_session)
    {
        long ZES_adminId = ZES_adminAccess.ZES_requireAdmin(ZES_session);
        if (ZES_userId == ZES_adminId)
        {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "현재 로그인한 관리자 자신의 권한은 변경할 수 없습니다.");
        }
        if (ZES_users.ZES_countUsersById(ZES_userId) != 1)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
        Long ZES_roleId = ZES_users.ZES_findRoleIdByCode(ZES_request.ZES_roleCode());
        if (ZES_roleId == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "부여할 권한을 찾을 수 없습니다.");
        }
        ZES_users.ZES_deleteUserRoles(ZES_userId);
        ZES_users.ZES_insertUserRole(ZES_userId, ZES_roleId);
        return Map.of("userId", ZES_userId, "roleCode", ZES_request.ZES_roleCode());
    }
}

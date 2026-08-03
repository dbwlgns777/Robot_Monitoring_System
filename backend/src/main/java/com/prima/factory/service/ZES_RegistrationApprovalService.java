package com.prima.factory.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.prima.factory.mapper.ZES_UserMapper;

@Service
public class ZES_RegistrationApprovalService
{
    private final ZES_UserMapper ZES_users;

    public ZES_RegistrationApprovalService(ZES_UserMapper ZES_users)
    {
        this.ZES_users = ZES_users;
    }

    public List<Map<String, Object>> ZES_pendingRegistrations(HttpSession ZES_session)
    {
        ZES_requireAdmin(ZES_session);
        return ZES_users.ZES_findPendingRegistrations();
    }

    @Transactional
    public Map<String, Object> ZES_approve(long ZES_registrationId, HttpSession ZES_session)
    {
        long ZES_reviewerId = ZES_requireAdmin(ZES_session);
        Map<String, Object> ZES_registration = ZES_getPendingRegistration(ZES_registrationId);
        String ZES_username = String.valueOf(ZES_registration.get("username"));
        if (ZES_users.ZES_countUsers(ZES_username) > 0)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 생성된 사용자 아이디입니다.");
        }

        Long ZES_roleId = ZES_users.ZES_findRoleIdByName(
            String.valueOf(ZES_registration.get("requestedRole")));
        if (ZES_roleId == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "신청한 권한을 찾을 수 없습니다.");
        }

        Map<String, Object> ZES_user = new HashMap<>();
        ZES_user.put("ZES_username", ZES_username);
        ZES_user.put("ZES_fullName", ZES_registration.get("fullName"));
        ZES_user.put("ZES_passwordHash", ZES_registration.get("passwordHash"));
        if (ZES_users.ZES_insertApprovedUser(ZES_user) != 1)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 계정을 생성하지 못했습니다.");
        }

        long ZES_userId = ((Number) ZES_user.get("ZES_userId")).longValue();
        ZES_users.ZES_insertUserRole(ZES_userId, ZES_roleId);
        if (ZES_users.ZES_updateRegistrationStatus(
            ZES_registrationId, "APPROVED", ZES_reviewerId) != 1)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 처리된 가입 신청입니다.");
        }
        return Map.of("id", ZES_registrationId, "status", "APPROVED", "userId", ZES_userId);
    }

    @Transactional
    public Map<String, Object> ZES_reject(long ZES_registrationId, HttpSession ZES_session)
    {
        long ZES_reviewerId = ZES_requireAdmin(ZES_session);
        ZES_getPendingRegistration(ZES_registrationId);
        if (ZES_users.ZES_updateRegistrationStatus(
            ZES_registrationId, "REJECTED", ZES_reviewerId) != 1)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 처리된 가입 신청입니다.");
        }
        return Map.of("id", ZES_registrationId, "status", "REJECTED");
    }

    private Map<String, Object> ZES_getPendingRegistration(long ZES_registrationId)
    {
        Map<String, Object> ZES_registration =
            ZES_users.ZES_findRegistrationForUpdate(ZES_registrationId);
        if (ZES_registration == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "가입 신청을 찾을 수 없습니다.");
        }
        if (!"PENDING".equals(ZES_registration.get("status")))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 처리된 가입 신청입니다.");
        }
        return ZES_registration;
    }

    private long ZES_requireAdmin(HttpSession ZES_session)
    {
        Object ZES_userId = ZES_session.getAttribute("USER_ID");
        Object ZES_roles = ZES_session.getAttribute("USER_ROLES");
        if (!(ZES_userId instanceof Number)
            || !(ZES_roles instanceof List<?> ZES_roleList)
            || !ZES_roleList.contains("ROLE_ADMIN"))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다.");
        }
        return ((Number) ZES_userId).longValue();
    }
}

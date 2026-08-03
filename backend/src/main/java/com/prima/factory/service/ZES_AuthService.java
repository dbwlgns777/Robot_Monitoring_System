package com.prima.factory.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.prima.factory.dto.ZES_LoginRequest;
import com.prima.factory.dto.ZES_SignupRequest;
import com.prima.factory.mapper.ZES_UserMapper;

@Service
public class ZES_AuthService
{
    private final ZES_UserMapper ZES_users;
    private final PasswordEncoder ZES_encoder;

    public ZES_AuthService(ZES_UserMapper ZES_users, PasswordEncoder ZES_encoder)
    {
        this.ZES_users = ZES_users;
        this.ZES_encoder = ZES_encoder;
    }

    public Map<String, Object> ZES_login(ZES_LoginRequest ZES_request, HttpSession ZES_session)
    {
        Map<String, Object> ZES_user = ZES_users.ZES_findByUsername(ZES_request.ZES_username());
        if (ZES_user == null || !ZES_encoder.matches(
            ZES_request.ZES_password(), String.valueOf(ZES_user.get("passwordHash"))))
        {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        if (!"APPROVED".equals(ZES_user.get("approvalStatus"))
            || Boolean.TRUE.equals(ZES_user.get("isLocked"))
            || Boolean.FALSE.equals(ZES_user.get("isActive")))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "승인되지 않았거나 잠긴 계정입니다.");
        }

        long ZES_userId = ((Number) ZES_user.get("id")).longValue();
        List<String> ZES_roles = ZES_users.ZES_findRoleCodes(ZES_userId);
        if (ZES_roles == null || ZES_roles.isEmpty())
        {
            ZES_roles = List.of("ROLE_USER");
        }
        var ZES_authorities = ZES_roles.stream().map(SimpleGrantedAuthority::new).toList();
        var ZES_authentication = UsernamePasswordAuthenticationToken.authenticated(
            ZES_user.get("username"), null, ZES_authorities);
        var ZES_securityContext = SecurityContextHolder.createEmptyContext();
        ZES_securityContext.setAuthentication(ZES_authentication);
        SecurityContextHolder.setContext(ZES_securityContext);
        ZES_session.setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, ZES_securityContext);
        ZES_session.setAttribute("USER_ID", ZES_user.get("id"));

        ZES_session.setAttribute("USER_ROLES", ZES_roles);

        return Map.of("id", ZES_user.get("id"), "username", ZES_user.get("username"),
            "name", ZES_user.get("fullName"), "roles", ZES_roles);
    }

    public void ZES_logout(HttpSession ZES_session)
    {
        SecurityContextHolder.clearContext();
        ZES_session.invalidate();
    }

    public Map<String, Object> ZES_currentUser(HttpSession ZES_session)
    {
        Object ZES_userId = ZES_session.getAttribute("USER_ID");
        if (ZES_userId == null)
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return Map.of("id", ZES_userId);
    }

    @Transactional
    public Map<String, Object> ZES_signup(ZES_SignupRequest ZES_request)
    {
        if (!ZES_request.ZES_password().equals(ZES_request.ZES_passwordConfirm()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호 확인이 일치하지 않습니다.");
        }
        if (ZES_users.ZES_countUsers(ZES_request.ZES_username()) > 0
            || ZES_users.ZES_countPendingRequests(ZES_request.ZES_username()) > 0)
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중이거나 승인 대기 중인 아이디입니다.");
        }

        Long ZES_factoryId = ZES_users.ZES_findFactoryId(ZES_request.ZES_factory());
        if (ZES_factoryId == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "등록된 공장을 선택해 주세요.");
        }

        Map<String, Object> ZES_registration = new HashMap<>();
        ZES_registration.put("ZES_username", ZES_request.ZES_username());
        ZES_registration.put("ZES_name", ZES_request.ZES_name());
        ZES_registration.put("ZES_passwordHash", ZES_encoder.encode(ZES_request.ZES_password()));
        ZES_registration.put("ZES_factoryId", ZES_factoryId);
        ZES_registration.put("ZES_department", ZES_request.ZES_department());
        ZES_registration.put("ZES_position", ZES_request.ZES_position());
        ZES_registration.put("ZES_phone", ZES_request.ZES_phone());
        ZES_registration.put("ZES_email", ZES_request.ZES_email());
        ZES_registration.put("ZES_requestedRole", ZES_request.ZES_requestedRole());

        int ZES_insertedRows = ZES_users.ZES_insertRegistration(ZES_registration);
        if (ZES_insertedRows != 1)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "가입 신청을 저장하지 못했습니다.");
        }
        return Map.of("status", "PENDING");
    }
}

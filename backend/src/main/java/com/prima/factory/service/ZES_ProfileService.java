package com.prima.factory.service;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.prima.factory.dto.ZES_ProfileUpdateRequest;
import com.prima.factory.dto.ZES_PasswordChangeRequest;
import com.prima.factory.mapper.ZES_UserMapper;

@Service
public class ZES_ProfileService
{
    private final ZES_UserMapper ZES_users;
    private final PasswordEncoder ZES_encoder;

    public ZES_ProfileService(ZES_UserMapper ZES_users, PasswordEncoder ZES_encoder)
    {
        this.ZES_users = ZES_users;
        this.ZES_encoder = ZES_encoder;
    }

    public Map<String, Object> ZES_profile(HttpSession ZES_session)
    {
        long ZES_userId = ZES_userId(ZES_session);
        Map<String, Object> ZES_profile = ZES_users.ZES_findProfile(ZES_userId);
        if (ZES_profile == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다.");
        }
        return ZES_profile;
    }

    @Transactional
    public Map<String, Object> ZES_update(
        ZES_ProfileUpdateRequest ZES_request, HttpSession ZES_session)
    {
        long ZES_userId = ZES_userId(ZES_session);
        Map<String, Object> ZES_profile = new HashMap<>();
        ZES_profile.put("ZES_userId", ZES_userId);
        ZES_profile.put("ZES_name", ZES_request.ZES_name());
        ZES_profile.put("ZES_factoryId", ZES_request.ZES_factoryId());
        ZES_profile.put("ZES_department", ZES_request.ZES_department());
        ZES_profile.put("ZES_position", ZES_request.ZES_position());
        ZES_profile.put("ZES_phone", ZES_request.ZES_phone());
        ZES_profile.put("ZES_email", ZES_request.ZES_email());
        if (ZES_users.ZES_updateProfile(ZES_profile) != 1)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 정보를 수정하지 못했습니다.");
        }
        return ZES_profile(ZES_session);
    }

    @Transactional
    public void ZES_changePassword(
        ZES_PasswordChangeRequest ZES_request, HttpSession ZES_session)
    {
        long ZES_userId = ZES_userId(ZES_session);
        String ZES_currentHash = ZES_users.ZES_findPasswordHash(ZES_userId);
        if (ZES_currentHash == null
            || !ZES_encoder.matches(ZES_request.ZES_currentPassword(), ZES_currentHash))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "현재 비밀번호가 올바르지 않습니다.");
        }
        if (!ZES_request.ZES_newPassword().equals(ZES_request.ZES_newPasswordConfirm()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "새 비밀번호 확인이 일치하지 않습니다.");
        }
        if (ZES_encoder.matches(ZES_request.ZES_newPassword(), ZES_currentHash))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "새 비밀번호는 현재 비밀번호와 달라야 합니다.");
        }
        if (ZES_users.ZES_updatePassword(
            ZES_userId, ZES_encoder.encode(ZES_request.ZES_newPassword())) != 1)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "비밀번호를 변경하지 못했습니다.");
        }
    }

    private long ZES_userId(HttpSession ZES_session)
    {
        Object ZES_userId = ZES_session.getAttribute("USER_ID");
        if (!(ZES_userId instanceof Number))
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return ((Number) ZES_userId).longValue();
    }
}

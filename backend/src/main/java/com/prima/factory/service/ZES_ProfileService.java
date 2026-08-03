package com.prima.factory.service;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.prima.factory.dto.ZES_ProfileUpdateRequest;
import com.prima.factory.mapper.ZES_UserMapper;

@Service
public class ZES_ProfileService
{
    private final ZES_UserMapper ZES_users;

    public ZES_ProfileService(ZES_UserMapper ZES_users)
    {
        this.ZES_users = ZES_users;
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

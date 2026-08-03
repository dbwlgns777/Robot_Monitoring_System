package com.prima.factory.service;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ZES_AdminAccessService
{
    public long ZES_requireAdmin(HttpSession ZES_session)
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

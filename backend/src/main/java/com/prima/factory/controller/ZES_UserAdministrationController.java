package com.prima.factory.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prima.factory.dto.ZES_ApiResponse;
import com.prima.factory.dto.ZES_RegistrationApprovalRequest;
import com.prima.factory.service.ZES_UserAdministrationService;

@RestController
@RequestMapping("/api/v1/admin/users")
public class ZES_UserAdministrationController
{
    private final ZES_UserAdministrationService ZES_userAdministrationService;

    public ZES_UserAdministrationController(
        ZES_UserAdministrationService ZES_userAdministrationService)
    {
        this.ZES_userAdministrationService = ZES_userAdministrationService;
    }

    @GetMapping
    ZES_ApiResponse<?> ZES_users(HttpSession ZES_session)
    {
        return ZES_ApiResponse.ZES_ok(ZES_userAdministrationService.ZES_users(ZES_session));
    }

    @GetMapping("/roles")
    ZES_ApiResponse<?> ZES_roles(HttpSession ZES_session)
    {
        return ZES_ApiResponse.ZES_ok(ZES_userAdministrationService.ZES_roles(ZES_session));
    }

    @PutMapping("/{ZES_userId}/role")
    ZES_ApiResponse<?> ZES_updateRole(
        @PathVariable("ZES_userId") long ZES_userId,
        @Valid @RequestBody ZES_RegistrationApprovalRequest ZES_request,
        HttpSession ZES_session)
    {
        return ZES_ApiResponse.ZES_ok(
            ZES_userAdministrationService.ZES_updateRole(ZES_userId, ZES_request, ZES_session));
    }
}

package com.prima.factory.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.prima.factory.dto.ZES_ApiResponse;
import com.prima.factory.dto.ZES_RegistrationApprovalRequest;
import com.prima.factory.service.ZES_RegistrationApprovalService;

@RestController
@RequestMapping("/api/v1/admin/registration-requests")
public class ZES_RegistrationApprovalController
{
    private final ZES_RegistrationApprovalService ZES_approvalService;

    public ZES_RegistrationApprovalController(ZES_RegistrationApprovalService ZES_approvalService)
    {
        this.ZES_approvalService = ZES_approvalService;
    }

    @GetMapping
    ZES_ApiResponse<?> ZES_pending(HttpSession ZES_session)
    {
        return ZES_ApiResponse.ZES_ok(ZES_approvalService.ZES_pendingRegistrations(ZES_session));
    }

    @GetMapping("/roles")
    ZES_ApiResponse<?> ZES_roles(HttpSession ZES_session)
    {
        return ZES_ApiResponse.ZES_ok(ZES_approvalService.ZES_assignableRoles(ZES_session));
    }

    @PostMapping("/{ZES_registrationId}/approve")
    ZES_ApiResponse<?> ZES_approve(
        @PathVariable("ZES_registrationId") long ZES_registrationId,
        @Valid @RequestBody ZES_RegistrationApprovalRequest ZES_request,
        HttpSession ZES_session)
    {
        return ZES_ApiResponse.ZES_ok(
            ZES_approvalService.ZES_approve(ZES_registrationId, ZES_request, ZES_session));
    }

    @PostMapping("/{ZES_registrationId}/reject")
    ZES_ApiResponse<?> ZES_reject(
        @PathVariable("ZES_registrationId") long ZES_registrationId,
        HttpSession ZES_session)
    {
        return ZES_ApiResponse.ZES_ok(
            ZES_approvalService.ZES_reject(ZES_registrationId, ZES_session));
    }
}

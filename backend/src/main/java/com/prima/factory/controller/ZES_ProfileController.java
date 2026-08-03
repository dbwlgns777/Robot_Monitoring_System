package com.prima.factory.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prima.factory.dto.ZES_ApiResponse;
import com.prima.factory.dto.ZES_ProfileUpdateRequest;
import com.prima.factory.service.ZES_ProfileService;

@RestController
@RequestMapping("/api/v1/profile")
public class ZES_ProfileController
{
    private final ZES_ProfileService ZES_profileService;

    public ZES_ProfileController(ZES_ProfileService ZES_profileService)
    {
        this.ZES_profileService = ZES_profileService;
    }

    @GetMapping
    ZES_ApiResponse<?> ZES_profile(HttpSession ZES_session)
    {
        return ZES_ApiResponse.ZES_ok(ZES_profileService.ZES_profile(ZES_session));
    }

    @PutMapping
    ZES_ApiResponse<?> ZES_update(
        @Valid @RequestBody ZES_ProfileUpdateRequest ZES_request, HttpSession ZES_session)
    {
        return ZES_ApiResponse.ZES_ok(ZES_profileService.ZES_update(ZES_request, ZES_session));
    }
}

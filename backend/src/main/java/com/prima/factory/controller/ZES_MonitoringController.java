package com.prima.factory.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prima.factory.dto.ApiResponse;
import com.prima.factory.service.ZES_MonitoringService;

@RestController
@RequestMapping("/api/v1")
public class ZES_MonitoringController
{
    private final ZES_MonitoringService ZES_monitoringService;

    public ZES_MonitoringController(ZES_MonitoringService ZES_monitoringService)
    {
        this.ZES_monitoringService = ZES_monitoringService;
    }

    @GetMapping("/factories") ApiResponse<?> ZES_factories()
    {
        return ApiResponse.ok(ZES_monitoringService.ZES_factories());
    }

    @GetMapping("/lines") ApiResponse<?> ZES_lines()
    {
        return ApiResponse.ok(ZES_monitoringService.ZES_lines());
    }

    @GetMapping("/dashboard/summary") ApiResponse<?> ZES_dashboard()
    {
        return ApiResponse.ok(ZES_monitoringService.ZES_dashboard());
    }

    @GetMapping({"/realtime/equipment", "/equipment"}) ApiResponse<?> ZES_equipment()
    {
        return ApiResponse.ok(ZES_monitoringService.ZES_equipment());
    }

    @GetMapping("/realtime/equipment/{ZES_id}") ApiResponse<?> ZES_equipment(@PathVariable("ZES_id") long ZES_id)
    {
        return ApiResponse.ok(ZES_monitoringService.ZES_equipment(ZES_id));
    }

    @GetMapping("/realtime/equipment/{ZES_id}/trend") ApiResponse<?> ZES_trend(@PathVariable("ZES_id") long ZES_id)
    {
        return ApiResponse.ok(ZES_monitoringService.ZES_trend(ZES_id));
    }

    @PostMapping("/equipment") ApiResponse<?> ZES_createEquipment(@RequestBody Map<String, Object> ZES_body)
    {
        return ApiResponse.ok(ZES_monitoringService.ZES_createEquipment(ZES_body));
    }

    @PutMapping("/equipment/{ZES_id}") ApiResponse<?> ZES_updateEquipment(
        @PathVariable("ZES_id") long ZES_id, @RequestBody Map<String, Object> ZES_body)
    {
        return ApiResponse.ok(ZES_monitoringService.ZES_updateEquipment(ZES_id, ZES_body));
    }

    @PatchMapping("/equipment/{ZES_id}/deactivate") ApiResponse<?> ZES_deactivateEquipment(
        @PathVariable("ZES_id") long ZES_id)
    {
        return ApiResponse.ok(ZES_monitoringService.ZES_deactivateEquipment(ZES_id));
    }

    @GetMapping("/products") ApiResponse<?> ZES_products()
    {
        return ApiResponse.ok(ZES_monitoringService.ZES_products());
    }

    @PostMapping("/products") ApiResponse<?> ZES_createProduct(@RequestBody Map<String, Object> ZES_body)
    {
        return ApiResponse.ok(ZES_monitoringService.ZES_createProduct(ZES_body));
    }

    @PutMapping("/products/{ZES_id}") ApiResponse<?> ZES_updateProduct(
        @PathVariable("ZES_id") long ZES_id, @RequestBody Map<String, Object> ZES_body)
    {
        return ApiResponse.ok(ZES_monitoringService.ZES_updateProduct(ZES_id, ZES_body));
    }

    @PatchMapping("/products/{ZES_id}/deactivate") ApiResponse<?> ZES_deactivateProduct(
        @PathVariable("ZES_id") long ZES_id)
    {
        return ApiResponse.ok(ZES_monitoringService.ZES_deactivateProduct(ZES_id));
    }

    @GetMapping("/analytics/production") ApiResponse<?> ZES_production()
    {
        return ApiResponse.ok(ZES_monitoringService.ZES_productionAnalytics());
    }

    @GetMapping({"/analytics/downtime", "/analytics/bottleneck"}) ApiResponse<?> ZES_downtime()
    {
        return ApiResponse.ok(ZES_monitoringService.ZES_downtimeAnalytics());
    }

    @GetMapping("/analytics/alarms") ApiResponse<?> ZES_alarms()
    {
        return ApiResponse.ok(ZES_monitoringService.ZES_alarms());
    }

    @GetMapping("/maintenance") ApiResponse<?> ZES_maintenance()
    {
        return ApiResponse.ok(ZES_monitoringService.ZES_maintenance());
    }

    @GetMapping("/system/collection-health") ApiResponse<?> ZES_collection()
    {
        return ApiResponse.ok(ZES_monitoringService.ZES_collection());
    }

    @GetMapping("/system/health-summary") ApiResponse<?> ZES_health()
    {
        return ApiResponse.ok(Map.of("services", List.of("BACKEND", "DEVICE_SERVER", "MYSQL")));
    }
}

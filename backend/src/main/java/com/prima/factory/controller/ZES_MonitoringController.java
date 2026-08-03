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

import com.prima.factory.dto.ZES_ApiResponse;
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

    @GetMapping("/factories")
    ZES_ApiResponse<?> ZES_factories()
    {
        return ZES_ApiResponse.ZES_ok(ZES_monitoringService.ZES_factories());
    }

    @GetMapping("/lines")
    ZES_ApiResponse<?> ZES_lines()
    {
        return ZES_ApiResponse.ZES_ok(ZES_monitoringService.ZES_lines());
    }

    @GetMapping("/dashboard/summary")
    ZES_ApiResponse<?> ZES_dashboard()
    {
        return ZES_ApiResponse.ZES_ok(ZES_monitoringService.ZES_dashboard());
    }

    @GetMapping({"/realtime/equipment", "/equipment"})
    ZES_ApiResponse<?> ZES_equipment()
    {
        return ZES_ApiResponse.ZES_ok(ZES_monitoringService.ZES_equipment());
    }

    @GetMapping("/realtime/equipment/{ZES_id}")
    ZES_ApiResponse<?> ZES_equipment(@PathVariable("ZES_id") long ZES_id)
    {
        return ZES_ApiResponse.ZES_ok(ZES_monitoringService.ZES_equipment(ZES_id));
    }

    @GetMapping("/realtime/equipment/{ZES_id}/trend")
    ZES_ApiResponse<?> ZES_trend(@PathVariable("ZES_id") long ZES_id)
    {
        return ZES_ApiResponse.ZES_ok(ZES_monitoringService.ZES_trend(ZES_id));
    }

    @PostMapping("/equipment")
    ZES_ApiResponse<?> ZES_createEquipment(@RequestBody Map<String, Object> ZES_body)
    {
        return ZES_ApiResponse.ZES_ok(ZES_monitoringService.ZES_createEquipment(ZES_body));
    }

    @PutMapping("/equipment/{ZES_id}")
    ZES_ApiResponse<?> ZES_updateEquipment(
        @PathVariable("ZES_id") long ZES_id, @RequestBody Map<String, Object> ZES_body)
    {
        return ZES_ApiResponse.ZES_ok(ZES_monitoringService.ZES_updateEquipment(ZES_id, ZES_body));
    }

    @PatchMapping("/equipment/{ZES_id}/deactivate")
    ZES_ApiResponse<?> ZES_deactivateEquipment(
        @PathVariable("ZES_id") long ZES_id)
    {
        return ZES_ApiResponse.ZES_ok(ZES_monitoringService.ZES_deactivateEquipment(ZES_id));
    }

    @GetMapping("/products")
    ZES_ApiResponse<?> ZES_products()
    {
        return ZES_ApiResponse.ZES_ok(ZES_monitoringService.ZES_products());
    }

    @PostMapping("/products")
    ZES_ApiResponse<?> ZES_createProduct(@RequestBody Map<String, Object> ZES_body)
    {
        return ZES_ApiResponse.ZES_ok(ZES_monitoringService.ZES_createProduct(ZES_body));
    }

    @PutMapping("/products/{ZES_id}")
    ZES_ApiResponse<?> ZES_updateProduct(
        @PathVariable("ZES_id") long ZES_id, @RequestBody Map<String, Object> ZES_body)
    {
        return ZES_ApiResponse.ZES_ok(ZES_monitoringService.ZES_updateProduct(ZES_id, ZES_body));
    }

    @PatchMapping("/products/{ZES_id}/deactivate")
    ZES_ApiResponse<?> ZES_deactivateProduct(
        @PathVariable("ZES_id") long ZES_id)
    {
        return ZES_ApiResponse.ZES_ok(ZES_monitoringService.ZES_deactivateProduct(ZES_id));
    }

    @GetMapping("/analytics/production")
    ZES_ApiResponse<?> ZES_production()
    {
        return ZES_ApiResponse.ZES_ok(ZES_monitoringService.ZES_productionAnalytics());
    }

    @GetMapping({"/analytics/downtime", "/analytics/bottleneck"})
    ZES_ApiResponse<?> ZES_downtime()
    {
        return ZES_ApiResponse.ZES_ok(ZES_monitoringService.ZES_downtimeAnalytics());
    }

    @GetMapping("/analytics/alarms")
    ZES_ApiResponse<?> ZES_alarms()
    {
        return ZES_ApiResponse.ZES_ok(ZES_monitoringService.ZES_alarms());
    }

    @GetMapping("/maintenance")
    ZES_ApiResponse<?> ZES_maintenance()
    {
        return ZES_ApiResponse.ZES_ok(ZES_monitoringService.ZES_maintenance());
    }

    @GetMapping("/system/collection-health")
    ZES_ApiResponse<?> ZES_collection()
    {
        return ZES_ApiResponse.ZES_ok(ZES_monitoringService.ZES_collection());
    }

    @GetMapping("/system/health-summary")
    ZES_ApiResponse<?> ZES_health()
    {
        return ZES_ApiResponse.ZES_ok(Map.of("services", List.of("BACKEND", "DEVICE_SERVER", "MYSQL")));
    }
}

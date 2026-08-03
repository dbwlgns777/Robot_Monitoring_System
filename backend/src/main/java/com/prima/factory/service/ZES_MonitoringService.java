package com.prima.factory.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prima.factory.mapper.ZES_MonitoringMapper;

@Service
public class ZES_MonitoringService
{
    private final ZES_MonitoringMapper ZES_mapper;

    public ZES_MonitoringService(ZES_MonitoringMapper ZES_mapper)
    {
        this.ZES_mapper = ZES_mapper;
    }

    public List<Map<String, Object>> ZES_factories()
    {
        return ZES_mapper.ZES_factories();
    }

    public List<Map<String, Object>> ZES_lines()
    {
        return ZES_mapper.ZES_lines();
    }

    public List<Map<String, Object>> ZES_equipment()
    {
        return ZES_mapper.ZES_currentEquipment();
    }

    public Map<String, Object> ZES_equipment(long ZES_id)
    {
        return ZES_mapper.ZES_equipment(ZES_id);
    }

    public List<Map<String, Object>> ZES_trend(long ZES_id)
    {
        return ZES_mapper.ZES_trend(ZES_id, 60);
    }

    public List<Map<String, Object>> ZES_collection()
    {
        return ZES_mapper.ZES_collectionHealth();
    }

    public List<Map<String, Object>> ZES_products()
    {
        return ZES_mapper.ZES_products();
    }

    public Map<String, Object> ZES_dashboard()
    {
        List<Map<String, Object>> ZES_equipment = ZES_equipment();
        long ZES_running = ZES_equipment.stream()
            .filter(ZES_item -> "RUNNING".equals(ZES_item.get("status")))
            .count();
        long ZES_alarms = ZES_equipment.stream()
            .filter(ZES_item -> String.valueOf(ZES_item.get("status")).contains("FAULT"))
            .count();
        return Map.of("equipment", ZES_equipment, "running", ZES_running,
            "alarms", ZES_alarms, "generatedAt", OffsetDateTime.now());
    }

    @Transactional
    public Map<String, Object> ZES_createEquipment(Map<String, Object> ZES_body)
    {
        ZES_mapper.ZES_insertEquipment(ZES_body);
        return ZES_body;
    }

    @Transactional
    public Map<String, Object> ZES_updateEquipment(long ZES_id, Map<String, Object> ZES_body)
    {
        ZES_mapper.ZES_updateEquipment(ZES_id, ZES_body);
        return ZES_body;
    }

    @Transactional
    public Map<String, Object> ZES_deactivateEquipment(long ZES_id)
    {
        ZES_mapper.ZES_deactivateEquipment(ZES_id);
        return Map.of("id", ZES_id, "active", false);
    }

    @Transactional
    public Map<String, Object> ZES_createProduct(Map<String, Object> ZES_body)
    {
        ZES_mapper.ZES_insertProduct(ZES_body);
        return ZES_body;
    }

    @Transactional
    public Map<String, Object> ZES_updateProduct(long ZES_id, Map<String, Object> ZES_body)
    {
        ZES_mapper.ZES_updateProduct(ZES_id, ZES_body);
        return ZES_body;
    }

    @Transactional
    public Map<String, Object> ZES_deactivateProduct(long ZES_id)
    {
        ZES_mapper.ZES_deactivateProduct(ZES_id);
        return Map.of("id", ZES_id, "active", false);
    }

    public List<Map<String, Object>> ZES_productionAnalytics()
    {
        return ZES_mapper.ZES_productionAnalytics();
    }

    public List<Map<String, Object>> ZES_downtimeAnalytics()
    {
        return ZES_mapper.ZES_downtimeAnalytics();
    }

    public List<Map<String, Object>> ZES_alarms()
    {
        return ZES_mapper.ZES_alarms();
    }

    public List<Map<String, Object>> ZES_maintenance()
    {
        return ZES_mapper.ZES_maintenance();
    }
}

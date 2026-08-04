package com.prima.factory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prima.factory.mapper.ZES_MonitoringMapper;

class ZES_MonitoringServiceTest
{
    @Test
    void ZES_equipmentParsesUnknownDynamicTagsWithoutFixedSchema()
    {
        ZES_MonitoringMapper ZES_mapper = mock(ZES_MonitoringMapper.class);
        when(ZES_mapper.ZES_currentEquipment()).thenReturn(List.of(Map.of(
            "id", 1L,
            "dynamicTags", "{\"vendor.anyNumber\":12.5,\"vendor.anyFlag\":true,\"vendor.anyText\":\"ok\"}")));

        Map<String, Object> ZES_equipment = new ZES_MonitoringService(
            ZES_mapper, new ObjectMapper()).ZES_equipment().get(0);
        @SuppressWarnings("unchecked")
        Map<String, Object> ZES_tags =
            (Map<String, Object>) ZES_equipment.get("dynamicTags");

        assertEquals(12.5, ZES_tags.get("vendor.anyNumber"));
        assertEquals(true, ZES_tags.get("vendor.anyFlag"));
        assertEquals("ok", ZES_tags.get("vendor.anyText"));
    }
}

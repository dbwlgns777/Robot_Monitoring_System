package com.prima.factory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class KpiServiceTest
{
    private final ZES_KpiService ZES_service = new ZES_KpiService();

    @Test
    void ZES_kpis()
    {
        assertEquals(80, ZES_service.ZES_achievement(800, 1000));
        assertEquals(200, ZES_service.ZES_hourly(400, 2));
        assertEquals(87.5, ZES_service.ZES_uptime(420, 480));
        assertEquals(120, ZES_service.ZES_loss(30, 240));
        assertEquals(100, ZES_service.ZES_mtbf(500, 5));
        assertEquals(12, ZES_service.ZES_mttr(60, 5));
    }

    @Test
    void ZES_zeroAndUnlinked()
    {
        assertEquals(0, ZES_service.ZES_achievement(1, 0));
        assertNull(ZES_service.ZES_mtbf(1, 0));
        assertFalse((Boolean) ZES_service.ZES_quality(false, 0, 0, 0, 0).get("qualityDataLinked"));
    }
}

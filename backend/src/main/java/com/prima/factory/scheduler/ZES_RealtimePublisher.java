package com.prima.factory.scheduler;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.prima.factory.service.ZES_MonitoringService;

@Component
public class ZES_RealtimePublisher
{
    private final ZES_MonitoringService ZES_monitoringService;
    private final SimpMessagingTemplate ZES_messagingTemplate;

    public ZES_RealtimePublisher(
        ZES_MonitoringService ZES_monitoringService, SimpMessagingTemplate ZES_messagingTemplate)
    {
        this.ZES_monitoringService = ZES_monitoringService;
        this.ZES_messagingTemplate = ZES_messagingTemplate;
    }

    @Scheduled(fixedRateString = "${monitoring.realtime-publish-ms:1000}")
    void ZES_publish()
    {
        var ZES_equipment = ZES_monitoringService.ZES_equipment();
        ZES_messagingTemplate.convertAndSend("/topic/equipment-status", ZES_equipment);
        ZES_messagingTemplate.convertAndSend("/topic/dashboard-kpi", ZES_monitoringService.ZES_dashboard());
        ZES_messagingTemplate.convertAndSend(
            "/topic/collection-health", ZES_monitoringService.ZES_collection());
    }
}

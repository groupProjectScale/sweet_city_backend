package com.example.services.kafka;

import com.example.model.Heartbeat;
import com.example.services.MonitoringService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private final MonitoringService monitoringService;
    private final String COORDINATE_TRACE_TOPIC = "heartbeat-monitor";

    public KafkaConsumer(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @KafkaListener(topics = COORDINATE_TRACE_TOPIC, groupId = "heartbeat-trace")
    public void listenToPartition(@Payload Heartbeat heartbeat) {
        monitoringService.process(heartbeat);
    }
}

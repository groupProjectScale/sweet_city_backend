package com.example.services.kafka;

import com.example.configurations.KafkaProducerConfiguration;
import com.example.model.Heartbeat;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, Heartbeat> kafkaTemplate;

    private static final String COORDINATE_TRACE_TOPIC = "heartbeat-monitor";

    public KafkaProducer(KafkaProducerConfiguration kafkaProducerConfiguration) {
        this.kafkaTemplate = kafkaProducerConfiguration.kafkaTemplate();
    }

    public void sendHeartbeat(Heartbeat heartbeat) {
        kafkaTemplate.send(COORDINATE_TRACE_TOPIC, heartbeat);
    }
}

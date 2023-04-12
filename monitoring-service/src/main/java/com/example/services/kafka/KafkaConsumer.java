package com.example.services.kafka;

import com.example.model.Heartbeat;
import com.example.model.HeartbeatCassandraModel;
import com.example.repository.HeartbeatRepository;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private final HeartbeatRepository heartbeatRepository;
    private final String COORDINATE_TRACE_TOPIC = "heartbeat-monitor";

    public KafkaConsumer(HeartbeatRepository heartbeatRepository) {
        this.heartbeatRepository = heartbeatRepository;
    }

    @KafkaListener(topics = COORDINATE_TRACE_TOPIC, groupId = "heartbeat-trace")
    public void listenToPartition(@Payload Heartbeat heartbeat) {

        HeartbeatCassandraModel h = new HeartbeatCassandraModel();
        BeanUtils.copyProperties(heartbeat, h);
        h.setId(UUID.randomUUID());
        heartbeatRepository.save(h);
        System.out.println(heartbeat.getService());
    }
}

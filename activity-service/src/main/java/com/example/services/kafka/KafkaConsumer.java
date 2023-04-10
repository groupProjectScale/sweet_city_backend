package com.example.services.kafka;

import com.example.model.Heartbeat;
import com.example.model.HeartbeatCassandraModel;
import com.example.repository.HeartbeatRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KafkaConsumer {

    private final HeartbeatRepository heartbeatRepository;
    private final String COORDINATE_TRACE_TOPIC = "heartbeat-monitor";

    public KafkaConsumer(HeartbeatRepository heartbeatRepository) {
        this.heartbeatRepository = heartbeatRepository;
    }

    @KafkaListener(topics = COORDINATE_TRACE_TOPIC, groupId = "heartbeat-trace")
    public void listenToPartition(
            @Payload Heartbeat heartbeat) {
        System.out.println(
                "Trace coordinate: " + heartbeat);

        HeartbeatCassandraModel h = new HeartbeatCassandraModel();
        BeanUtils.copyProperties(heartbeat, h);
        h.setId(UUID.randomUUID());
        heartbeatRepository.save(h);
        System.out.println("success");

    }

//    private HeartbeatCassandraModel composeTracer(CoordinateTrace trace) {
//        var model = new CoordinateTracerCassandraModel();
//        model.setUserId((int)trace.getUserId());
//        model.setX(trace.getX());
//        model.setY(trace.getY());
//        model.setId(UUID.randomUUID());
//        System.out.println(trace.getTimestamp());
//        model.setTs(Instant.ofEpochMilli(trace.getTimestamp()));
//        return model;
//    }
}

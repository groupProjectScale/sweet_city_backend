package com.example.services;

import com.example.model.Heartbeat;
import com.example.proto.MonitoringServiceGrpc;
import com.example.proto.MonitoringServiceOuterClass;
import com.example.services.kafka.KafkaProducer;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

public class MonitoringServiceImpl extends MonitoringServiceGrpc.MonitoringServiceImplBase {
    @Autowired private KafkaProducer kafkaProducer;

    @Override
    public void send(
            MonitoringServiceOuterClass.HeartbeatRequest request,
            StreamObserver<MonitoringServiceOuterClass.HeartbeatResponse> responseObserver) {
        System.out.println("success");
        Heartbeat heartbeat =
                new Heartbeat(request.getName(), request.getIsRunning(), request.getTimeStamp());
        kafkaProducer.sendHeartbeat(heartbeat);
    }
}

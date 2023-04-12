package com.example.services;

import com.example.model.Heartbeat;
import com.example.services.kafka.KafkaProducer;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proto.HeartbeatRequest;
import proto.HeartbeatResponse;
import proto.MonitoringServiceGrpc;

@GrpcService
public class MonitoringServerServiceImpl extends MonitoringServiceGrpc.MonitoringServiceImplBase {

    private KafkaProducer kafkaProducer;
    private static final Logger logger = LogManager.getLogger(MonitoringServerServiceImpl.class);

    public MonitoringServerServiceImpl(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);

    @Override
    public void send(HeartbeatRequest request, StreamObserver<HeartbeatResponse> responseObserver) {
        Future<Heartbeat> future =
                executor.submit(
                        () -> {
                            Heartbeat heartbeat =
                                    new Heartbeat(
                                            request.getName(),
                                            request.getIsRunning(),
                                            request.getTimeStamp());
                            return heartbeat;
                        });

        try {
            Heartbeat heartbeat = future.get();
            kafkaProducer.sendHeartbeat(heartbeat);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }
}

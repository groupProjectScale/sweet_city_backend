package com.example.services;

import io.grpc.stub.StreamObserver;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proto.HeartbeatRequest;
import proto.HeartbeatResponse;
import proto.MonitoringServiceGrpc;

// @Service
public class MonitoringClientService {
    @GrpcClient("activity")
    MonitoringServiceGrpc.MonitoringServiceStub stub;

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private static final Logger logger = LogManager.getLogger(MonitoringClientService.class);
    private final String SERVICE_NAME = "activity";

    public MonitoringClientService() {
        sendHeartBeat();
    }

    private void sendHeartBeat() {
        scheduler.scheduleAtFixedRate(this::sendHeartBeatMessage, 0, 10000, TimeUnit.MILLISECONDS);
    }

    private void sendHeartBeatMessage() {
        try {
            HeartbeatRequest request =
                    HeartbeatRequest.newBuilder()
                            .setName(SERVICE_NAME)
                            .setIsRunning(true)
                            .setTimeStamp(System.currentTimeMillis())
                            .build();
            stub.send(
                    request,
                    new StreamObserver<HeartbeatResponse>() {
                        @Override
                        public void onNext(HeartbeatResponse response) {}

                        @Override
                        public void onError(Throwable t) {
                            logger.error(t.getMessage());
                        }

                        @Override
                        public void onCompleted() {}
                    });
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
}

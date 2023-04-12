package com.example.services;

import com.example.model.Heartbeat;
import com.example.model.HeartbeatCassandraModel;
import com.example.repository.HeartbeatRepository;
import com.example.services.kafka.KafkaProducer;
import io.grpc.stub.StreamObserver;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import proto.HeartbeatRequest;
import proto.HeartbeatResponse;
import proto.MonitoringServiceGrpc;

@GrpcService
public class MonitoringService extends MonitoringServiceGrpc.MonitoringServiceImplBase {

    private KafkaProducer kafkaProducer;
    private final HeartbeatRepository heartbeatRepository;
    private static final Logger logger = LogManager.getLogger(MonitoringService.class);

    public MonitoringService(KafkaProducer kafkaProducer, HeartbeatRepository heartbeatRepository) {
        this.kafkaProducer = kafkaProducer;
        this.heartbeatRepository = heartbeatRepository;
    }

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);

    @Override
    public void send(HeartbeatRequest request, StreamObserver<HeartbeatResponse> responseObserver) {
        Heartbeat heartbeat =
                new Heartbeat(request.getName(), request.getIsRunning(), request.getTimeStamp());
        kafkaProducer.sendHeartbeat(heartbeat);
    }

    public Future<?> process(Heartbeat heartbeat) {
        Future<HeartbeatCassandraModel> future =
                executor.submit(
                        () -> {
                            HeartbeatCassandraModel h = new HeartbeatCassandraModel();
                            BeanUtils.copyProperties(heartbeat, h);
                            h.setId(UUID.randomUUID());
                            HeartbeatCassandraModel res = heartbeatRepository.save(h);
                            return res;
                        });
        try {
            HeartbeatCassandraModel res = future.get();
            logger.info(res.getService() + " runs successfully");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}

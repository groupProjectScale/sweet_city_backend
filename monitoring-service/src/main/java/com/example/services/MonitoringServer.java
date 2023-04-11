package com.example.services;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import java.io.IOException;

public class MonitoringServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server =
                ServerBuilder.forPort(50051)
                        .addService(new MonitoringServiceImpl())
                        .addService(ProtoReflectionService.newInstance()) // reflection
                        .build();

        server.start();

        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(
                                () -> {
                                    System.out.println("Received Shutdown Request");
                                    server.shutdown();
                                    System.out.println("Successfully stopped the server");
                                }));

        server.awaitTermination();
    }
}

package com.example.repository;

import com.example.model.HeartbeatCassandraModel;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface HeartbeatRepository extends CassandraRepository<HeartbeatCassandraModel, UUID> {
}

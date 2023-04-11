package com.example.repository;

import com.example.model.HeartbeatCassandraModel;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface HeartbeatRepository extends CassandraRepository<HeartbeatCassandraModel, UUID> {}

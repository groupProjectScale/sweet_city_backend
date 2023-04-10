CREATE KEYSPACE IF NOT EXISTS sweetcity_cassandra WITH replication = {'class' : 'SimpleStrategy', 'replication_factor' : 1};
USE "sweetcity_cassandra";
CREATE TABLE IF NOT EXISTS "heartbeat_monitor" (
    id uuid,
    service text,
    is_running boolean,
    time_stamp bigint,
    PRIMARY KEY (id)
);
DESCRIBE TABLES;

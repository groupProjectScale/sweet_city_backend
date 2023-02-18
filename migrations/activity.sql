CREATE TABLE IF NOT EXISTS activity  (
    activity_id UUID NOT NULL DEFAULT uuid_generate_v4(),
    name VARCHAR(20) NOT NULL,
    user_id UUID NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    location_id UUID,
    price NUMERIC(16, 4) DEFAULT 0 NOT NULL,
    current_participants Int,
    minimum_participants Int,
    maximum_participants Int,
    PRIMARY KEY (activity_id),
    CONSTRAINT future_date CHECK (start_time > current_timestamp),
    CONSTRAINT later_than_start CHECK (end_time > start_time)
);


INSERT INTO activity (name, user_id, start_time, end_time) VALUES ('hiking', uuid_generate_v4(), current_timestamp + interval '3' day, current_timestamp + interval '4' day);

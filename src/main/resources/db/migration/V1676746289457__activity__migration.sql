CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS postgis;


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


CREATE TABLE IF NOT EXISTS activity_attendee (
     activity_id UUID NOT NULL,
     user_id UUID NOT NULL,
     joined_time TIMESTAMP NOT NULL,
     PRIMARY KEY (activity_id, user_id)
);

CREATE TABLE IF NOT EXISTS activity_tag (
    activity_id UUID NOT NULL,
    tag_id UUID NOT NULL
);
CREATE TABLE IF NOT EXISTS activity_requirement (
    activity_id UUID NOT NULL,
    requirement_id UUID NOT NULL
);

CREATE TABLE IF NOT EXISTS address(
  address_id UUID NOT NULL DEFAULT uuid_generate_v4(),
/*point_id SERIAL PRIMARY KEY,*/
  user_id UUID NOT NULL,
  location VARCHAR(255) NOT NULL,
  longitude numeric NOT NULL,
  latitude numeric NOT NULL,
  geo geometry(POINT),
  PRIMARY KEY (address_id)
);


INSERT INTO activity (name, user_id, start_time, end_time) VALUES ('hiking', uuid_generate_v4(), current_timestamp + interval '3' day, current_timestamp + interval '4' day);
INSERT INTO address(location, longitude, latitude, geo) VALUES ('Seattle',47.608013,-122.335167,ST_Point(47.608013, -122.335167));


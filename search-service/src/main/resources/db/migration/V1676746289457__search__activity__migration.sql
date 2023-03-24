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

CREATE TABLE IF NOT EXISTS tag (
    tag_id UUID NOT NULL DEFAULT uuid_generate_v4(),
    tag_description VARCHAR(255) NOT NULL,
    num_of_creations Int
);


CREATE TABLE IF NOT EXISTS location(
    location_id UUID NOT NULL DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    longitude numeric NOT NULL,
    latitude numeric NOT NULL,
    geo geometry(POINT),
    PRIMARY KEY (location_id)
);



CREATE TABLE IF NOT EXISTS activity_tag (
    activity_id UUID NOT NULL,
    tag_id UUID NOT NULL,
    PRIMARY KEY (activity_id, tag_id)
);

CREATE TABLE IF NOT EXISTS requirement(
                                          requirement_id UUID NOT NULL DEFAULT uuid_generate_v4(),
                                          description VARCHAR(255) NOT NULL
);



CREATE TABLE IF NOT EXISTS activity_attendee (
                                                 activity_id UUID NOT NULL,
                                                 user_id UUID NOT NULL,
                                                 joined_time TIMESTAMP NOT NULL,
                                                 PRIMARY KEY (activity_id, user_id)
);



CREATE TABLE IF NOT EXISTS activity_requirement (
                                                    activity_id UUID NOT NULL,
                                                    requirement_id UUID NOT NULL,
                                                    PRIMARY KEY (activity_id, requirement_id)
);

CREATE TABLE IF NOT EXISTS address(
                                      address_id UUID NOT NULL DEFAULT uuid_generate_v4(),
                                      user_id UUID NOT NULL,
                                      location VARCHAR(255) NOT NULL,
                                      longitude numeric NOT NULL,
                                      latitude numeric NOT NULL,
                                      geo geometry(POINT),
                                      PRIMARY KEY (address_id)
);


CREATE TABLE IF NOT EXISTS client (
                                      user_id UUID NOT NULL DEFAULT uuid_generate_v4(),
                                      user_name VARCHAR(255) NOT NULL UNIQUE,
                                      first_name VARCHAR(255) NOT NULL,
                                      last_name VARCHAR(255) NOT NULL,
                                      email VARCHAR(255) NOT NULL,
                                      hash_password_with_salt VARCHAR(255) NOT NULL
);



-- Testdata for searchByNearbyLocation, searchByTagDescriptions

INSERT INTO location(location_id, name, latitude, longitude, geo) VALUES ('a3eff582-2af8-498b-bca4-f771c4bd60dd', 'home',37.7796,-122.4089,ST_Point(37.7796, -122.4089));
INSERT INTO location(location_id, name, latitude, longitude, geo) VALUES ('39d8449b-a0e2-4ae8-9a06-3c5703681432', 'park1',37.7621,-122.4342,ST_Point(37.7621, -122.4342));
INSERT INTO location(location_id, name, latitude, longitude, geo) VALUES ('3ab70190-cb08-4a2d-bbea-d08666fb9df1', 'park2',37.7972,-122.4471,ST_Point(37.7972, -122.4471));
INSERT INTO location(location_id, name, latitude, longitude, geo) VALUES ('a6fd268b-5c47-4ee4-b30f-899b16a889f0', 'park3',37.7401,-122.3927,ST_Point(37.7401, -122.3927));

INSERT INTO activity (activity_id, name, user_id, start_time, end_time, location_id, current_participants) VALUES ('039e4c15-6769-4b1a-aa4d-8df1742a7057', 'hiking', uuid_generate_v4(), current_timestamp + interval '3' day, current_timestamp + interval '4' day, '39d8449b-a0e2-4ae8-9a06-3c5703681432', 1);
INSERT INTO activity (activity_id, name, user_id, start_time, end_time, location_id, current_participants) VALUES ('af51b898-e669-4610-b564-301e9e287638', 'swimming', uuid_generate_v4(), current_timestamp + interval '3' day, current_timestamp + interval '4' day, '3ab70190-cb08-4a2d-bbea-d08666fb9df1', 2);
INSERT INTO activity (activity_id, name, user_id, start_time, end_time, location_id, current_participants) VALUES ('95ec78a4-fe9d-452f-baed-1eb674d4a358', 'running', uuid_generate_v4(), current_timestamp + interval '3' day, current_timestamp + interval '4' day, 'a6fd268b-5c47-4ee4-b30f-899b16a889f0', 3);
--INSERT INTO activity (name, user_id, start_time, end_time, location_id, current_attendees) VALUES ( 'planting', uuid_generate_v4(), current_timestamp + interval '3' day, current_timestamp + interval '4' day, 'a6fd268b-5c47-4ee4-b30f-899b16a889f0', 3);

INSERT INTO tag(tag_id, tag_description, num_of_creations) values ('65ee9ec8-dbe4-4d92-8b23-95459959ced0', 'hikingisfun', 6);
INSERT INTO tag(tag_id, tag_description, num_of_creations) values ('72ec7f27-be51-4242-9072-1f4856ca4742', 'hiking', 4);
INSERT INTO tag(tag_id, tag_description, num_of_creations) values ('c59808b3-614e-40d8-8e4b-c3ded0d85620', 'swimming', 3);
INSERT INTO activity_tag(activity_id, tag_id) values ('039e4c15-6769-4b1a-aa4d-8df1742a7057', '65ee9ec8-dbe4-4d92-8b23-95459959ced0');
INSERT INTO activity_tag(activity_id, tag_id) values ('039e4c15-6769-4b1a-aa4d-8df1742a7057', '72ec7f27-be51-4242-9072-1f4856ca4742');
INSERT INTO activity_tag(activity_id, tag_id) values ('af51b898-e669-4610-b564-301e9e287638', 'c59808b3-614e-40d8-8e4b-c3ded0d85620');

UPDATE address SET geo = ST_Point(longitude, latitude);
UPDATE location SET geo = ST_Point(longitude, latitude);



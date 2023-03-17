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

CREATE TABLE IF NOT EXISTS requirement(
    requirement_id UUID NOT NULL DEFAULT uuid_generate_v4(),
    description VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS location(
    location_id UUID NOT NULL DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    longitude numeric NOT NULL,
    latitude numeric NOT NULL,
    geo geometry(POINT),
    PRIMARY KEY (location_id)
);


CREATE TABLE IF NOT EXISTS activity_attendee (
     activity_id UUID NOT NULL,
     user_id UUID NOT NULL,
     joined_time TIMESTAMP NOT NULL,
     PRIMARY KEY (activity_id, user_id)
);

CREATE TABLE IF NOT EXISTS activity_tag (
    activity_id UUID NOT NULL,
    tag_id UUID NOT NULL,
    PRIMARY KEY (activity_id, tag_id)
);

CREATE TABLE IF NOT EXISTS activity_requirement (
    activity_id UUID NOT NULL,
    requirement_id UUID NOT NULL,
    PRIMARY KEY (activity_id, requirement_id)
);

CREATE TABLE IF NOT EXISTS address(
    address_id UUID NOT NULL DEFAULT uuid_generate_v4(),
/*point_id SERIAL PRIMARY KEY, */
    user_id UUID NOT NULL,
    location VARCHAR(255) NOT NULL,
    longitude numeric NOT NULL,
    latitude numeric NOT NULL,
    geo geometry(POINT),
    PRIMARY KEY (address_id)
);

CREATE TABLE IF NOT EXISTS image(
    image_id UUID NOT NULL DEFAULT uuid_generate_v4(),
    file_name VARCHAR(255) NOT NULL,
    activity_id_plus_file_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (image_id)
);

CREATE TABLE IF NOT EXISTS activity_image (
     activity_id UUID NOT NULL,
     image_id UUID NOT NULL,
     PRIMARY KEY (activity_id, image_id)
);

-- INSERT INTO activity (name, user_id, start_time, end_time) VALUES ('hiking', uuid_generate_v4(), current_timestamp + interval '3' day, current_timestamp + interval '4' day);
-- INSERT INTO address(location, longitude, latitude, geo) VALUES ('Seattle',47.608013,-122.335167,ST_Point(47.608013, -122.335167));
-- INSERT INTO activity (activity_id, price, name, user_id, start_time, end_time, current_participants) VALUES (uuid_generate_v4(), 0, 'hiking', uuid_generate_v4(), current_timestamp + interval '3' day, current_timestamp + interval '4' day, 1);




-- Testing for getranking db setup
-- first insert a new user -> use user_id to insert a new address -> use this address_id to
--insert into location with same address_id as its location_id -> then insert 3 records into
--location -> use respective location_id to create activities

--INSERT INTO location(name, longitude, latitude, geo) VALUES ('park1',47.608013,-122.335167,ST_Point(47.608013, -122.335167));
--INSERT INTO location(name, longitude, latitude, geo) VALUES ('park2',48.608013,-121.335167,ST_Point(48.608013, -121.335167));
--INSERT INTO location(name, longitude, latitude, geo) VALUES ('park3',49.608013,-121.335167,ST_Point(49.608013, -121.335167));

--INSERT INTO activity (name, user_id, start_time, end_time, location_id, current_participants) VALUES ('hiking', uuid_generate_v4(), current_timestamp + interval '3' day, current_timestamp + interval '4' day, '7f6af58f-4191-44e6-97bc-e58d4aeb0e3e', 1);
--INSERT INTO activity (name, user_id, start_time, end_time, location_id, current_participants) VALUES ('swimming', uuid_generate_v4(), current_timestamp + interval '3' day, current_timestamp + interval '4' day, 'c74ba297-3a0c-4e3b-a397-61a9fecfc09c', 2);
--INSERT INTO activity (name, user_id, start_time, end_time, location_id, current_participants) VALUES ('running', uuid_generate_v4(), current_timestamp + interval '3' day, current_timestamp + interval '4' day, '2f7bed3a-f553-47a7-8e65-186b9e43c13f', 3);

--INSERT INTO location(location_id,name, longitude, latitude, geo) VALUES ('bcaa80de-6ee2-4f07-804a-2bdfd59b6634','home',46.608013,-122.335167,ST_Point(46.608013, -122.335167));
--INSERT INTO address(user_id, location, longitude, latitude) values ('9fd9e8cc-5fd1-4063-ae3e-bbbaca0083d2', 'home', 46.608013, -122.335167);
INSERT INTO tag(tag_description, num_of_creations) values ('hikingisfun', 6);

UPDATE address SET geo = ST_Point(longitude, latitude);
UPDATE location SET geo = ST_Point(longitude, latitude);


CREATE TABLE IF NOT EXISTS client (
    user_id UUID NOT NULL DEFAULT uuid_generate_v4(),
    user_name VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    hash_password_with_salt VARCHAR(255) NOT NULL
);
--used for getranking testing
--insert into client(user_name, first_name, last_name, email, hash_password_with_salt) values ('1bobbboy','bodkljfb','jkdhfd','gamijfdk@gmail.com','djkgfhdsjkg')
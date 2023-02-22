CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE IF NOT EXISTS client (
        user_id UUID NOT NULL DEFAULT uuid_generate_v4(),
        user_name VARCHAR(255) NOT NULL UNIQUE,
        first_name VARCHAR(255) NOT NULL,
        last_name VARCHAR(255) NOT NULL,
        email VARCHAR(255) NOT NULL,
        hash_password_with_salt VARCHAR(255) NOT NULL
);

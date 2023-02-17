CREATE TABLE IF NOT EXISTS client (
        user_id UUID NOT NULL DEFAULT uuid_generate_v4(),
        username VARCHAR(255) NOT NULL UNIQUE,
        firstname VARCHAR(255) NOT NULL,
        lastname VARCHAR(255) NOT NULL,
        email VARCHAR(255) NOT NULL,
        password VARCHAR(255) NOT NULL
);

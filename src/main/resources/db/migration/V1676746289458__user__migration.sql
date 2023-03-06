CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
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

DROP TABLE IF EXISTS authorized_users;

CREATE TABLE IF NOT EXISTS authorized_users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(255) NOT NULL
);

INSERT INTO authorized_users (id, username) VALUES ('86a2992a-bcd3-4bb6-abf2-e9e14b380c91','admin');

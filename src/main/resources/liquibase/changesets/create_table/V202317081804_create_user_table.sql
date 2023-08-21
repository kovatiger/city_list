CREATE TABLE IF NOT EXISTS users
(
    user_id     UUID         PRIMARY KEY,
    login       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL
);
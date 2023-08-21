CREATE TABLE IF NOT EXISTS country
(
    country_id      UUID            PRIMARY KEY,
    country_name     VARCHAR(255)    NOT NULL,
    logo_name        VARCHAR(255)    NOT NULL
);
CREATE TABLE IF NOT EXISTS city
(
    city_id         UUID            PRIMARY KEY,
    country_id      UUID            NOT NULL,
    city            VARCHAR(255)    NOT NULL,
    constraint fk_cities_country FOREIGN KEY (country_id) references country (country_id) ON DELETE CASCADE
);
CREATE TABLE airports (
    id BIGSERIAL PRIMARY KEY,
    city_name VARCHAR(150) NOT NULL,
    city_code VARCHAR(3)  NOT NULL
);
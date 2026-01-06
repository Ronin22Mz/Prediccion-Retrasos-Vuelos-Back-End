CREATE TABLE airlines (
    id        BIGSERIAL PRIMARY KEY,
    airline_name VARCHAR(150) NOT NULL,
    airline_code VARCHAR(3)   NOT NULL
);
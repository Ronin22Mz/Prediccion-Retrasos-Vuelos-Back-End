CREATE TABLE airline_origin_airport
(
    id                BIGSERIAL PRIMARY KEY,
    airline_id        BIGINT NOT NULL,
    origin_airport_id BIGINT NOT NULL,
    CONSTRAINT fk_airline_origin_airport_airline
        FOREIGN KEY (airline_id)
            REFERENCES airlines (id),
    CONSTRAINT fk_airline_origin_airport_origin_airport
        FOREIGN KEY (origin_airport_id)
            REFERENCES airports (id)
);


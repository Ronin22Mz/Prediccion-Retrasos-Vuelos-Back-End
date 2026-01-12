CREATE TABLE airline_destination_airport
(
    id                BIGSERIAL PRIMARY KEY,
    airline_id        BIGINT NOT NULL,
    destination_airport_id BIGINT NOT NULL,
    CONSTRAINT fk_airline_destination_airport_airline
        FOREIGN KEY (airline_id)
            REFERENCES airlines (id),
    CONSTRAINT fk_airline_destination_airport_origin_airport
        FOREIGN KEY (destination_airport_id)
            REFERENCES airports (id)
);


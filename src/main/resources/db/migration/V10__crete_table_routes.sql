CREATE TABLE routes
(
    id                     BIGSERIAL PRIMARY KEY,
    origin_airport_id      BIGINT           NOT NULL,
    destination_airport_id BIGINT           NOT NULL,
    distance               DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_routes_origin_airport
        FOREIGN KEY (origin_airport_id)
            REFERENCES airports (id),
    CONSTRAINT fk_routes_destination_airport
        FOREIGN KEY (destination_airport_id)
            REFERENCES airports (id)
);

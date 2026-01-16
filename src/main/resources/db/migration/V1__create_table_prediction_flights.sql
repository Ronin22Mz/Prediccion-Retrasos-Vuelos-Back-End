CREATE TABLE prediction_flights
(
    id                BIGSERIAL PRIMARY KEY,
    creation_date     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    airline           VARCHAR(2)               NOT NULL,
    origin            VARCHAR(3)               NOT NULL,
    destination       VARCHAR(3)               NOT NULL,
    departure_date    TIMESTAMP                NOT NULL,
    departure_hour    TIME NOT NULL,
    arrived_hour      TIME NOT NULL,
    distance_km       DOUBLE PRECISION         NOT NULL,
    elapsed_time      DOUBLE PRECISION NOT NULL,
    prediction_result VARCHAR(50)              NOT NULL,
    probability       DOUBLE PRECISION         NOT NULL
);
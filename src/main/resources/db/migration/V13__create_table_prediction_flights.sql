CREATE TABLE prediction_flights
(
    id                NUMBER PRIMARY KEY,
    creation_date     TIMESTAMP NOT NULL,
    airline           VARCHAR2(2)  NOT NULL,
    origin            VARCHAR2(3)  NOT NULL,
    destination       VARCHAR2(3)  NOT NULL,
    departure_date    TIMESTAMP NOT NULL,
    departure_hour    TIMESTAMP NOT NULL,
    arrived_hour      TIMESTAMP NULL,
    distance_km       NUMBER(10,2)    NOT NULL,
    elapsed_time      NUMBER(10,2)    NOT NULL,
    prediction_result VARCHAR2(50) NOT NULL,
    probability       NUMBER(5,4)   NOT NULL
);

CREATE SEQUENCE prediction_flights_seq
    START WITH 1
    INCREMENT BY 1 NOCACHE
    NOCYCLE;

CREATE
OR REPLACE TRIGGER prediction_flights_bi
BEFORE INSERT ON prediction_flights
FOR EACH ROW
BEGIN
    IF
:NEW.id IS NULL THEN
SELECT prediction_flights_seq.NEXTVAL
INTO :NEW.id
FROM dual;
END IF;
END;
/

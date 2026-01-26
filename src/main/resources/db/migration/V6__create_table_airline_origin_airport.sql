CREATE TABLE airline_origin_airport
(
    id                NUMBER PRIMARY KEY,
    airline_id        NUMBER NOT NULL,
    origin_airport_id NUMBER NOT NULL,

    CONSTRAINT fk_airline_origin_airline
        FOREIGN KEY (airline_id)
            REFERENCES airlines (id),

    CONSTRAINT fk_airline_origin_airport
        FOREIGN KEY (origin_airport_id)
            REFERENCES airports (id)
);

CREATE SEQUENCE airline_origin_airport_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

CREATE OR REPLACE TRIGGER airline_origin_airport_bi
BEFORE INSERT ON airline_origin_airport
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
SELECT airline_origin_airport_seq.NEXTVAL
INTO :NEW.id
FROM dual;
END IF;
END;
/
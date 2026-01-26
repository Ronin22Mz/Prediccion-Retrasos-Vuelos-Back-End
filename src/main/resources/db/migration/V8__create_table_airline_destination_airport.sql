CREATE TABLE airline_destination_airport
(
    id                     NUMBER PRIMARY KEY,
    airline_id             NUMBER NOT NULL,
    destination_airport_id NUMBER NOT NULL,

    CONSTRAINT fk_dest_airline
        FOREIGN KEY (airline_id)
            REFERENCES airlines (id),

    CONSTRAINT fk_dest_airport
        FOREIGN KEY (destination_airport_id)
            REFERENCES airports (id)
);

CREATE SEQUENCE airline_destination_airport_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

CREATE OR REPLACE TRIGGER airline_destination_airport_bi
BEFORE INSERT ON airline_destination_airport
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
SELECT airline_destination_airport_seq.NEXTVAL
INTO :NEW.id
FROM dual;
END IF;
END;
/

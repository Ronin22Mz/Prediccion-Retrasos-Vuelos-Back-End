CREATE TABLE routes
(
    id                     NUMBER PRIMARY KEY,
    origin_airport_id      NUMBER NOT NULL,
    destination_airport_id NUMBER NOT NULL,
    distance               FLOAT  NOT NULL,

    CONSTRAINT fk_routes_origin
        FOREIGN KEY (origin_airport_id)
            REFERENCES airports (id),

    CONSTRAINT fk_routes_dest
        FOREIGN KEY (destination_airport_id)
            REFERENCES airports (id)
);

CREATE SEQUENCE routes_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

CREATE OR REPLACE TRIGGER routes_bi
BEFORE INSERT ON routes
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
SELECT routes_seq.NEXTVAL
INTO :NEW.id
FROM dual;
END IF;
END;
/
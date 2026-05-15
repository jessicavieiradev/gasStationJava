CREATE TABLE fuel_pumps (
    id UUID NOT NULL,
    name VARCHAR(200) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    fuel_type_id UUID,

    CONSTRAINT pk_fuel_pumps PRIMARY KEY (id),
    CONSTRAINT fk_fuel_pumps_fuel_type FOREIGN KEY (fuel_type_id)
        REFERENCES fuel_types (id)
);
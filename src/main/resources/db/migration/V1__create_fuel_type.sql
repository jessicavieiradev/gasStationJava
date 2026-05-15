CREATE TABLE fuel_types (
    id UUID NOT NULL,
    name VARCHAR(200) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) DEFAULT 'ativo' NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT pk_fuel_types PRIMARY KEY (id)
);
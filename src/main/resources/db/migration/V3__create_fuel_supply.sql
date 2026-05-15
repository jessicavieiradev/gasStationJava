CREATE TABLE fuel_supplies (
   id UUID NOT NULL,
   date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   total_amount DECIMAL(10, 2) NOT NULL,
   literage DECIMAL(10, 3) NOT NULL,
   created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
   fuel_pump_id UUID NOT NULL,

   CONSTRAINT pk_fuel_supplies PRIMARY KEY (id),
   CONSTRAINT fk_fuel_supplies_fuel_pump FOREIGN KEY (fuel_pump_id)
       REFERENCES fuel_pumps (id)
);
CREATE TABLE gastos_operativos (
    id serial PRIMARY KEY,
    concepto VARCHAR(100),
    monto DECIMAL,
    fecha DATE
);

ALTER TABLE product ADD COLUMN costo_unitario DECIMAL(10,2);
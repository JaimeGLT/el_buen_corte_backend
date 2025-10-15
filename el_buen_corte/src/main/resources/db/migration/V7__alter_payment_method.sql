ALTER TABLE payment
ALTER COLUMN payment_method TYPE character varying(100)
USING payment_method::character varying(100);

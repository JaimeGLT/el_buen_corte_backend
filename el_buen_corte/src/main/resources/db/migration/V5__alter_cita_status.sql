ALTER TABLE appointment 
ALTER COLUMN status TYPE VARCHAR(50)
USING status::text;

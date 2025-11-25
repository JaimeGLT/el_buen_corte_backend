-- Agregar columna created_at a tabla client
ALTER TABLE client ADD COLUMN IF NOT EXISTS created_at DATE;

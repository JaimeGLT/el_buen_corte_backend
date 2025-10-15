ALTER TABLE Staff
ALTER COLUMN specialties TYPE character varying(100)[]
  USING specialties::character varying(100)[];

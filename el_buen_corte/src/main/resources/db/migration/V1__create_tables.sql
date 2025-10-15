-- ===== ENUMS =====
CREATE TYPE role_enum AS ENUM ('ADMINISTRADOR', 'RECEPCIONISTA', 'ESTILISTA');
CREATE TYPE appointment_status AS ENUM ('Pendiente', 'Confirmado', 'En proceso', 'Finalizado');
CREATE TYPE payment_method AS ENUM ('QR', 'Tarjeta', 'Efectivo');
CREATE TYPE movement_type AS ENUM ('Entrada', 'Salida');

-- ===== STAFF =====
CREATE TABLE staff (
                       id BIGSERIAL PRIMARY KEY,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       hairdresser_role VARCHAR(100),
                       email VARCHAR(250) NOT NULL UNIQUE,
                       phone_number VARCHAR(20) NOT NULL UNIQUE,
                       working_hours_start TIME,
                       working_hours_finish TIME,
                       specialties VARCHAR(200),
                       role role_enum NOT NULL
);

-- ===== CLIENT =====
CREATE TABLE client (
                        id BIGSERIAL PRIMARY KEY,
                        first_name VARCHAR(100) NOT NULL,
                        last_name VARCHAR(100) NOT NULL,
                        email VARCHAR(250),
                        phone_number VARCHAR(20) NOT NULL,
                        observations VARCHAR(500)
);

-- ===== SERVICE =====
CREATE TABLE service (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(150) NOT NULL,
                         description VARCHAR(250) NOT NULL,
                         type VARCHAR(100) NOT NULL,
                         price DECIMAL(10,2) NOT NULL,
                         duration INTERVAL NOT NULL
);

-- ===== APPOINTMENT =====
CREATE TABLE appointment (
                             id BIGSERIAL PRIMARY KEY,
                             date DATE NOT NULL,
                             time TIME NOT NULL,
                             notes TEXT,
                             status appointment_status DEFAULT 'Pendiente',
                             stylist_id BIGINT REFERENCES staff(id) ON DELETE SET NULL,
                             client_id BIGINT REFERENCES client(id) ON DELETE CASCADE,
                             service_id BIGINT REFERENCES service(id) ON DELETE CASCADE
);

-- ===== PAYMENT =====
CREATE TABLE payment (
                         id BIGSERIAL PRIMARY KEY,
                         amount DECIMAL(10,2) NOT NULL,
                         payment_method payment_method NOT NULL,
                         client_id BIGINT REFERENCES client(id) ON DELETE CASCADE,
                         service_id BIGINT REFERENCES service(id) ON DELETE SET NULL
);

-- ===== CATEGORY =====
CREATE TABLE category (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL
);

-- ===== PRODUCT =====
CREATE TABLE product (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         brand VARCHAR(100) NOT NULL,
                         initial_stock INTEGER NOT NULL,
                         minimum_stock INTEGER NOT NULL,
                         supplier VARCHAR(100),
                         category_id BIGINT REFERENCES category(id) ON DELETE SET NULL
);

-- ===== MOVEMENT =====
CREATE TABLE movement (
                          id BIGSERIAL PRIMARY KEY,
                          type movement_type NOT NULL,
                          quantity INTEGER NOT NULL,
                          reason VARCHAR(250) NOT NULL,
                          product_id BIGINT REFERENCES product(id) ON DELETE CASCADE
);

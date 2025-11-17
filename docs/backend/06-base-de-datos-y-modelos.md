# Base de Datos y Modelos

## Base de Datos
- **Tipo**: PostgreSQL (relacional).
- **Migraciones**: Gestionadas con Flyway, scripts en `src/main/resources/db/migration/`.
- **Conexión**: Configurada en `application.properties` (localhost:5432, usuario postgres).

## Migraciones Principales
- **V1__create_tables.sql**: Crea tablas principales (users, clients, citas, etc.).
- **V2__add_password_staff.sql**: Agrega contraseña a staff.
- **V3__alter_role_type.sql**: Modifica tipo de rol.
- **V4__alter_specialties_type.sql**: Cambia especialidades.
- **V5__alter_cita_status.sql**: Actualiza estado de citas.
- **V6__create_movement_table.sql**: Tabla de movimientos.
- **V7__alter_payment_method.sql**: Métodos de pago.
- **V8__add_column_payment.sql**: Columnas en pagos.
- **V9__add_date_column_product.sql**: Fecha en productos.
- **V10__add_price_column_product.sql**: Precio en productos.
- **V11__add_activeColumn_service.sql**: Activo en servicios.

## Modelos Principales (Entidades JPA)
- **User**: Usuario del sistema (staff), con roles (ADMIN, RECEPCIONISTA, ESTILISTA).
- **Client**: Cliente, con datos personales y observaciones.
- **Cita**: Cita, relaciona cliente, estilista, servicio, fecha, estado.
- **Service**: Servicio ofrecido (corte, tinte), con precio y duración.
- **Product**: Producto de inventario, con categoría, stock.
- **Category**: Categoría de productos.
- **Movement**: Movimiento de inventario (entrada/salida).
- **Payment**: Pago, asociado a cita/cliente, método (QR, TARJETA, EFECTIVO).

## Relaciones
- User 1:N Cita (estilista).
- Client 1:N Cita.
- Service 1:N Cita.
- Category 1:N Product.
- Product 1:N Movement.

## Repositorios
- Interfaces JPA extendiendo JpaRepository, con métodos custom si es necesario (ej: findByEmail en User).

## Notas
- Usuarios tienen roles para autorización.
- Citas tienen estados (PENDIENTE, CONFIRMADA, etc.).
- Flyway asegura consistencia en cambios de esquema.

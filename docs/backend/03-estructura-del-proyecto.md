# Estructura del Proyecto Backend

## Estructura General
```
el_buen_corte_backend/
├── el_buen_corte/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/el_buen_corte/el_buen_corte/
│   │   │   │   ├── ElBuenCorteApplication.java  # Clase principal
│   │   │   │   ├── auth/                        # Autenticación
│   │   │   │   │   ├── AuthenticationController.java
│   │   │   │   │   ├── AuthenticationService.java
│   │   │   │   │   ├── ...
│   │   │   │   ├── category/                    # Categorías de productos
│   │   │   │   ├── cita/                        # Citas
│   │   │   │   ├── client/                      # Clientes
│   │   │   │   ├── config/                      # Configuraciones
│   │   │   │   ├── movement/                    # Movimientos de inventario
│   │   │   │   ├── payment/                     # Pagos
│   │   │   │   ├── product/                     # Productos
│   │   │   │   ├── reports/                     # Reportes
│   │   │   │   ├── service/                     # Servicios
│   │   │   │   ├── Stylist/                     # Estilistas
│   │   │   │   └── user/                        # Usuarios
│   │   │   └── resources/
│   │   │       ├── application.properties       # Configuración
│   │   │       └── db/migration/                # Migraciones Flyway
│   │   └── test/
│   │       └── java/com/el_buen_corte/el_buen_corte/
│   │           └── ElBuenCorteApplicationTests.java
│   ├── pom.xml                                  # Dependencias Maven
│   ├── Dockerfile                               # Contenerización
│   ├── mvnw                                     # Wrapper Maven
│   └── .gitignore
```

## Descripción de Paquetes
- **auth/**: Maneja login, registro y JWT.
- **category/**: CRUD de categorías para productos.
- **cita/**: Gestión de citas (Appointment).
- **client/**: Gestión de clientes.
- **config/**: Configuraciones de seguridad, JWT, etc.
- **movement/**: Movimientos de inventario (entradas/salidas).
- **payment/**: Pagos asociados a servicios.
- **product/**: Productos del inventario.
- **reports/**: Consultas para reportes.
- **service/**: Servicios ofrecidos (cortes, etc.).
- **Stylist/**: Estilistas (personal).
- **user/**: Usuarios del sistema (staff).

## Archivos Importantes
- **pom.xml**: Define dependencias (Spring Boot, PostgreSQL, JWT, etc.).
- **application.properties**: Configuración de BD, JWT, etc.
- **Migraciones**: Scripts SQL en `db/migration/` para evolución del esquema.

Esta estructura modular facilita el desarrollo, con un paquete por módulo/entidad, siguiendo buenas prácticas de organización en Spring Boot.

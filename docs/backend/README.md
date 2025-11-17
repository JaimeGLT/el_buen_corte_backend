# Backend: el_buen_corte_backend

## Introducción
El backend del proyecto "proyecto_erp_el_buen_corte" es una API REST desarrollada con Spring Boot, diseñada para gestionar las operaciones de una peluquería llamada "El Buen Corte". Proporciona endpoints para autenticación, gestión de clientes, citas, servicios, inventario, pagos y reportes. Está construido como una aplicación monolítica con capas bien definidas para mantener la separación de responsabilidades.

## Arquitectura
- **Patrón**: MVC (Model-View-Controller) adaptado para APIs REST, con controladores manejando solicitudes HTTP, servicios conteniendo lógica de negocio y repositorios interactuando con la base de datos.
- **Autenticación y Seguridad**: Utiliza JWT (JSON Web Tokens) con Spring Security para autenticación y autorización. Incluye filtros personalizados para validar tokens en cada solicitud.
- **Migraciones de Base de Datos**: Gestionadas con Flyway, permitiendo versionar cambios en el esquema de PostgreSQL de forma incremental.
- **Estado**: Stateless, ideal para APIs REST, sin almacenamiento de sesión en el servidor.
- **Operaciones**: Soporta CRUD completo para entidades clave como staff, clientes, servicios, citas, pagos, productos y movimientos de inventario.

## Tecnologías Utilizadas
- **Lenguaje**: Java 17.
- **Framework Principal**: Spring Boot 3.5.6, incluyendo starters para JPA, Security y Web.
- **Base de Datos**: PostgreSQL, con JDBC para conexión.
- **ORM**: Spring Data JPA para mapeo objeto-relacional.
- **Seguridad**: Spring Security con JWT (librería jjwt versión 0.11.5).
- **Migraciones**: Flyway Core y Flyway Database PostgreSQL.
- **Herramientas Adicionales**:
  - Lombok: Para reducir código boilerplate (anotaciones como @Data, @Entity).
  - Maven: Para gestión de dependencias y build.
  - Docker: Incluye Dockerfile para contenedorización.
- **Pruebas**: Spring Boot Starter Test y Spring Security Test (limitado a tests básicos).

## Estructura del Proyecto
- **Raíz**: `el_buen_corte_backend/el_buen_corte/` (proyecto Maven).
- **Código Fuente** (`src/main/java/com/el_buen_corte/el_buen_corte/`):
  - `auth/`: Controladores, servicios y entidades para autenticación (e.g., AuthenticationController, AuthenticationService).
  - `category/`, `cita/`, `client/`, `service/`, `payment/`, `product/`, `movement/`: Entidades, controladores, servicios y repositorios para cada módulo ERP.
  - `config/`: Configuraciones (e.g., ApplicationConfig, JwtService, JwtAuthenticationFilter).
  - `user/`, `reports/`, `Stylist/`: Módulos adicionales.
- **Recursos** (`src/main/resources/`):
  - `application.properties`: Configuración de base de datos (PostgreSQL local), JWT secret y modo de inicialización SQL.
  - `db/migration/`: Scripts SQL para migraciones (e.g., V1__create_tables.sql define tablas con enums y relaciones).
- **Pruebas**: `src/test/java/` con tests de integración básicos.
- **Build**: `pom.xml` define dependencias y plugins de Maven.

## Configuración del Entorno
- **Requisitos**: Java 17, Maven, PostgreSQL.
- **Configuración**:
  - Base de datos: Configurada en `application.properties` (URL: jdbc:postgresql://localhost:5432/el_buen_corte, usuario: postgres, contraseña: "contraseña de la base de datos local"). Usa variables de entorno para producción.
  - JWT: Secret hardcoded en properties (recomendado cambiar a variables de entorno).
- **Ejecución**: `mvn spring-boot:run` o `./mvnw spring-boot:run`. Incluye Dockerfile para despliegue en contenedores.

## API y Endpoints
- **Base URL**: No especificada en configs (probablemente localhost:8080).
- **Endpoints Principales** (basado en controladores):
  - Autenticación: `/auth/login`, `/auth/register`.
  - Clientes: `/clients` (CRUD).
  - Citas: `/appointments` (CRUD, con estados como Pendiente, Confirmado).
  - Servicios: `/services` (CRUD).
  - Pagos: `/payments` (CRUD, con métodos QR, Tarjeta, Efectivo).
  - Inventario: `/products`, `/movements` (entradas/salidas).
  - Categorías: `/categories`.
- **Formato**: JSON. Incluye validaciones y manejo de errores básico.

## Base de Datos y Modelos
- **Base de Datos**: PostgreSQL.
- **Modelos Principales**:
  - `Staff`: Información de empleados (roles: ADMINISTRADOR, RECEPCIONISTA, ESTILISTA).
  - `Client`: Datos de clientes.
  - `Service`: Servicios ofrecidos (precio, duración).
  - `Appointment`: Citas con estado, estilista y cliente.
  - `Payment`: Pagos asociados a servicios/clientes.
  - `Product`: Productos de inventario con stock.
  - `Movement`: Movimientos de inventario (Entrada/Salida).
  - `Category`: Categorías de productos.
- **Relaciones**: Foreign keys entre tablas (e.g., cita -> cliente, servicio; pago -> cliente, servicio).
- **Enums**: role_enum, appointment_status, payment_method, movement_type.

## Proceso de Despliegue
- **Local**: Ejecutar con Maven o IDE (e.g., IntelliJ).
- **Contenedor**: Usar Dockerfile para build de imagen Docker.
- **Producción**: Configurar variables de entorno para BD y JWT. Desplegar en servidores como AWS EC2 o Heroku.

## Seguridad y Autenticación
- **JWT**: Tokens generados en login, validados en cada endpoint protegido.
- **Roles**: Basados en enum (ADMINISTRADOR, etc.), controlados en servicios.
- **Filtros**: JwtAuthenticationFilter intercepta solicitudes.
- **Recomendaciones**: Implementar HTTPS, rate limiting y validaciones adicionales.

## Mejores Prácticas
- Separación de capas (Controller -> Service -> Repository).
- Uso de Lombok para entidades.
- Migraciones versionadas con Flyway.
- Validaciones en entidades (e.g., @NotNull).
- **Mejoras Sugeridas**: Agregar tests exhaustivos, documentación API (Swagger), manejo de excepciones global y logging avanzado.

## Notas de Versión
- Versión actual: 0.0.1.
- Cambios principales: Migraciones de BD hasta V11, integración de JWT y Flyway.

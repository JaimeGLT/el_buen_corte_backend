# Arquitectura del Backend

## Patrón Arquitectónico
El backend sigue una arquitectura **monolítica** basada en el patrón **MVC (Model-View-Controller)**, con separación clara de capas para mantener el código organizado y mantenible.

### Capas Principales
1. **Controlador (Controller)**: Maneja las solicitudes HTTP entrantes, valida datos y delega la lógica a los servicios. Ejemplos: `AuthenticationController`, `ClientController`.
2. **Servicio (Service)**: Contiene la lógica de negocio, orquesta operaciones entre repositorios y aplica reglas de negocio. Ejemplos: `AuthenticationService`, `ClientService`.
3. **Repositorio (Repository)**: Interfaz con la base de datos usando JPA/Hibernate. Maneja consultas y operaciones CRUD. Ejemplos: `ClientRepository`, `CitaRepository`.
4. **Modelo (Model)**: Representa las entidades de negocio, mapeadas a tablas de BD. Ejemplos: `Client`, `Cita`, `User`.

## Arquitectura de Seguridad
- **Autenticación**: Basada en JWT, generados en `JwtService`.
- **Autorización**: Filtros como `JwtAuthenticationFilter` validan tokens y roles.
- **Configuración**: `ApplicationConfig` configura beans de seguridad y CORS.

## Base de Datos
- **PostgreSQL**: Base de datos relacional.
- **ORM**: JPA con Hibernate para mapeo objeto-relacional.
- **Migraciones**: Flyway gestiona versiones del esquema con scripts SQL numerados (V1__ a V11__).

## Comunicación
- **API REST**: Endpoints RESTful para CRUD y operaciones específicas.
- **Formato de Datos**: JSON para requests/responses.
- **CORS**: Configurado para permitir orígenes del frontend.

## Escalabilidad y Mantenibilidad
- **Stateless**: No mantiene estado de sesión, ideal para escalar horizontalmente.
- **Inyección de Dependencias**: Spring maneja dependencias automáticamente.
- **Lombok**: Reduce código repetitivo.
- **Excepciones**: Manejo centralizado (no visible en código actual, pero recomendado).

## Estructura de Paquetes
- `auth/`: Autenticación y JWT.
- `config/`: Configuraciones de aplicación y seguridad.
- `category/`, `cita/`, `client/`, etc.: Módulos por entidad.
- `resources/`: Propiedades y migraciones.

Esta arquitectura es simple, efectiva para una aplicación de tamaño mediano como un ERP para peluquería, permitiendo fácil mantenimiento y extensión.

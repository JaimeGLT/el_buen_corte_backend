# Tecnologías Utilizadas en el Backend

## Lenguaje y Framework Principal
- **Java 17**: Lenguaje de programación orientado a objetos, utilizado para el desarrollo de la aplicación.
- **Spring Boot 3.5.6**: Framework para crear aplicaciones Java basadas en Spring, facilita la configuración y el desarrollo rápido de APIs REST.

## Dependencias Clave
- **Spring Boot Starter Web**: Para crear aplicaciones web RESTful.
- **Spring Boot Starter Data JPA**: Para integración con bases de datos relacionales usando JPA/Hibernate.
- **Spring Boot Starter Security**: Para implementar autenticación y autorización.
- **PostgreSQL**: Base de datos relacional utilizada para persistencia de datos.
- **Flyway**: Herramienta para migraciones de base de datos, versionando cambios en el esquema.
- **JWT (JSON Web Tokens)**: Para manejo de autenticación stateless.
- **Lombok**: Librería para reducir código boilerplate en Java (genera getters, setters, constructores automáticamente).
- **Maven**: Herramienta de gestión de dependencias y construcción del proyecto.

## Herramientas de Desarrollo
- **Docker**: Para contenerización de la aplicación (Dockerfile presente).
- **Git**: Control de versiones.
- **IDE**: Compatible con IntelliJ IDEA, Eclipse o VS Code con extensiones Java.

## Base de Datos
- **PostgreSQL**: Sistema de gestión de bases de datos relacional, utilizado para almacenar datos de usuarios, clientes, citas, etc.
- **Migraciones**: Gestionadas con Flyway, con scripts SQL en `src/main/resources/db/migration/`.

## Seguridad
- **Spring Security**: Framework para autenticación y autorización.
- **JWT**: Tokens para sesiones seguras sin estado.

## Otras Librerías
- **Jackson**: Para serialización/deserialización JSON (incluido en Spring Boot).
- **Hibernate**: ORM subyacente en JPA para mapeo objeto-relacional.

Esta pila tecnológica permite un desarrollo robusto, escalable y seguro para una API de ERP.

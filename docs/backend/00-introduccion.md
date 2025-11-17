# Introducción al Backend

El backend del proyecto "proyecto_erp_el_buen_corte" es una API REST desarrollada con Spring Boot, diseñada para gestionar las operaciones de una peluquería llamada "El Buen Corte". Proporciona endpoints para autenticación, gestión de clientes, citas, servicios, inventario, pagos y reportes. Está construido como una aplicación monolítica con capas bien definidas para mantener la separación de responsabilidades.

## Propósito
- Gestionar datos de la peluquería de manera segura y eficiente.
- Proporcionar una API RESTful para el frontend.
- Integrar con una base de datos PostgreSQL para persistencia de datos.
- Implementar autenticación y autorización basada en JWT.

## Funcionalidades Principales
- **Autenticación**: Login y registro de usuarios (staff con roles como Administrador, Recepcionista, Estilista).
- **Gestión de Clientes**: CRUD de clientes con información personal y observaciones.
- **Citas**: Programación, confirmación y seguimiento de citas con estilistas y servicios.
- **Servicios**: Definición de servicios ofrecidos (cortes, tintes, etc.) con precios y duración.
- **Inventario**: Control de productos, categorías y movimientos (entradas/salidas).
- **Pagos**: Registro de pagos asociados a servicios/clientes con métodos como QR, Tarjeta, Efectivo.
- **Reportes**: Datos para análisis (no implementado directamente en backend, pero soporta consultas).

## Arquitectura General
- **Patrón MVC**: Controladores manejan solicitudes HTTP, servicios contienen lógica de negocio, repositorios acceden a BD.
- **Seguridad**: Spring Security con JWT para sesiones seguras.
- **Base de Datos**: PostgreSQL con Flyway para migraciones.
- **Estado**: Stateless, ideal para escalabilidad.

## Tecnologías Clave
- Java 17, Spring Boot 3.5.6, PostgreSQL, JWT, Flyway, Lombok, Maven.

Este backend actúa como el núcleo del sistema, asegurando integridad de datos y comunicación eficiente con el frontend.

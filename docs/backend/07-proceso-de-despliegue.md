# Proceso de Despliegue del Backend

## Despliegue Local
1. Asegurar Java 17, Maven y PostgreSQL instalados.
2. Clonar repo y navegar a directorio.
3. Ejecutar `./mvnw clean install`.
4. Configurar BD (crear base `el_buen_corte`).
5. Ejecutar `./mvnw spring-boot:run`.
6. API en `http://localhost:8080`.

## Despliegue con Docker
1. Construir imagen: `docker build -t el-buen-corte-backend .`
2. Ejecutar: `docker run -p 8080:8080 -e DB_URL=jdbc:postgresql://host:5432/db -e DB_USERNAME=user -e DB_PASSWORD=pass el-buen-corte-backend`
3. Asegurar BD externa accesible.

## Despliegue en Producción
- Usar variables de entorno para configuración sensible.
- Configurar reverse proxy (Nginx) para SSL y balanceo.
- Usar servicios como AWS RDS para BD, EC2 o ECS para app.
- Monitoreo con logs y herramientas como Spring Actuator.

## Notas
- Puerto configurable.
- Migraciones automáticas con Flyway.
- Recomendado usar contenedores para consistencia.

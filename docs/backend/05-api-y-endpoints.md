# API y Endpoints del Backend

## Base URL
La API está disponible en `http://localhost:8080/api/v1/` (asumiendo configuración por defecto).

## Autenticación
- **POST /auth/register**: Registrar nuevo usuario (staff).
- **POST /auth/login**: Iniciar sesión, retorna JWT.

## Clientes
- **GET /clients**: Listar clientes (paginado).
- **POST /clients**: Crear cliente.
- **GET /clients/{id}**: Obtener cliente por ID.
- **PUT /clients/{id}**: Actualizar cliente.
- **DELETE /clients/{id}**: Eliminar cliente.

## Citas
- **GET /citas**: Listar citas.
- **POST /citas**: Crear cita.
- **GET /citas/{id}**: Obtener cita.
- **PUT /citas/{id}**: Actualizar cita.
- **DELETE /citas/{id}**: Eliminar cita.

## Servicios
- **GET /services**: Listar servicios.
- **POST /services**: Crear servicio.
- **PUT /services/{id}**: Actualizar servicio.
- **DELETE /services/{id}**: Eliminar servicio.

## Productos
- **GET /products**: Listar productos.
- **POST /products**: Crear producto.
- **PUT /products/{id}**: Actualizar producto.
- **DELETE /products/{id}**: Eliminar producto.

## Categorías
- **GET /categories**: Listar categorías.
- **POST /categories**: Crear categoría.
- **PUT /categories/{id}**: Actualizar categoría.
- **DELETE /categories/{id}**: Eliminar categoría.

## Movimientos de Inventario
- **GET /movements**: Listar movimientos.
- **POST /movements**: Crear movimiento.

## Pagos
- **GET /payments**: Listar pagos.
- **POST /payments**: Crear pago.

## Personal (Estilistas)
- **GET /personal**: Listar personal.
- **POST /personal**: Crear personal.
- **PUT /personal/{id}**: Actualizar personal.
- **DELETE /personal/{id}**: Eliminar personal.

## Reportes
- Endpoints para consultas de reportes (no detallados en código actual).

## Headers Requeridos
- **Authorization: Bearer <jwt-token>**: Para endpoints protegidos (excepto login/register).

## Formato de Respuesta
- **Éxito**: JSON con datos.
- **Error**: JSON con mensaje de error, código HTTP apropiado (400, 401, 404, etc.).

## Notas
- Todos los endpoints requieren autenticación excepto `/auth/*`.
- Paginación en listas donde aplica.
- Validación de datos en requests.

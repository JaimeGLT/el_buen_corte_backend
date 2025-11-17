# Seguridad y Autenticación

## Autenticación
- Basada en JWT (JSON Web Tokens).
- Endpoints `/auth/login` y `/auth/register` no requieren token.
- Login retorna token JWT con claims (id, email, rol).

## Autorización
- Filtro `JwtAuthenticationFilter` valida token en cada request.
- Roles: ADMIN, RECEPCIONISTA, ESTILISTA (definidos en enum Role).
- Control de acceso basado en roles (ej: solo ADMIN para ciertos endpoints).

## Configuración de Seguridad
- `ApplicationConfig`: Configura CORS, desactiva CSRF (stateless), permite endpoints públicos.
- `JwtService`: Genera y valida tokens usando clave secreta.

## Buenas Prácticas
- Tokens con expiración (no visto en código, recomendado configurar).
- Contraseñas hasheadas (usar BCrypt, no implementado actualmente).
- Validación de inputs para prevenir inyección.
- HTTPS en producción.

## Notas
- Actualmente, contraseñas en texto plano (mejorar con encoding).
- Stateless, no sesiones en servidor.

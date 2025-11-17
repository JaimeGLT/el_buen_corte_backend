# Convenciones de Código y Sintaxis en el Backend

## Nomenclatura
- **Clases**: PascalCase (ej: `ElBuenCorteApplication`, `ClientController`).
- **Interfaces**: PascalCase (ej: `ClientRepository`).
- **Paquetes**: lowercase con puntos (ej: `com.el_buen_corte.el_buen_corte`).
- **Variables y Campos**: camelCase (ej: `clientName`, `userId`).
- **Métodos**: camelCase (ej: `findById()`, `saveClient()`).
- **Constantes**: UPPER_CASE con guiones bajos (ej: `JWT_SECRET`).
- **Enums**: PascalCase (ej: `Role`, `Status`).
- **Archivos**: PascalCase con extensión .java (ej: `Client.java`).

## Estructura de Archivos y Carpetas
- **Paquetes por entidad**: Cada módulo en su paquete (ej: `client/`, `auth/`).
- **Configuración**: En `config/` (ej: `ApplicationConfig.java`).
- **Recursos**: En `resources/` (properties, migraciones).
- **Tests**: En `test/java/` mirroring la estructura de `main/`.

## Sintaxis Común
- **Imports**: Organizados por paquetes estándar, luego terceros, luego propios.
  ```java
  import org.springframework.boot.SpringApplication;
  import com.el_buen_corte.el_buen_corte.ElBuenCorteApplication;
  ```
- **Anotaciones**: Sobre clases/métodos (ej: `@SpringBootApplication`, `@RestController`).
- **Inyección de Dependencias**: `@Autowired` o constructor (preferido).
- **Lombok**: `@Data`, `@AllArgsConstructor` para reducir código.
- **Excepciones**: Usar `@ControllerAdvice` para manejo global.
- **Logs**: `private static final Logger logger = LoggerFactory.getLogger(Class.class);`

## Buenas Prácticas
- Líneas no excedan 120 caracteres.
- Comentarios en inglés o español consistente.
- Usar `final` en parámetros inmutables.
- Validación con `@Valid` y `@NotNull`.

Esta convención asegura consistencia y mantenibilidad en el código Java/Spring.

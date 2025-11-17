# 12. Implementación de Swagger/OpenAPI

## Introducción

Swagger (ahora conocido como OpenAPI) es una herramienta que permite documentar y probar APIs REST de manera interactiva. En este documento se explica cómo implementar Swagger en el proyecto El Buen Corte Backend.

## ¿Qué es Swagger/OpenAPI?

Swagger es un conjunto de herramientas que permiten:
- Documentar APIs REST de forma automática
- Proporcionar una interfaz web interactiva para probar endpoints
- Generar documentación técnica de la API
- Facilitar la integración con otras herramientas de desarrollo

## Implementación Paso a Paso

### 1. Agregar Dependencia Maven

Para implementar Swagger, primero debemos agregar la dependencia correspondiente al archivo `pom.xml`:

```xml
<!-- SpringDoc OpenAPI para documentación de API con Swagger -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>
```

Esta dependencia incluye tanto la funcionalidad de OpenAPI como la interfaz de usuario de Swagger.

### 2. Crear Configuración de OpenAPI

Crear la clase `OpenApiConfig.java` en el paquete `config` para configurar la información básica de la API:

```java
package com.el_buen_corte.el_buen_corte.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("El Buen Corte API")
                        .description("API REST para el sistema de gestión de peluquería El Buen Corte")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("soporte@elbuencorte.com")));
    }
}
```

### 3. Actualizar Configuración de Seguridad

Modificar la clase `SecurityConfiguration.java` para permitir el acceso público a las rutas de Swagger:

```java
.authorizeHttpRequests(
    request -> request.requestMatchers("/api/v1/auth/**", "/api/v1/client/**", "/api/v1/ai/chat",
            "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html")
            .permitAll()
            .anyRequest().authenticated())
```

### 4. Compilar y Ejecutar

Después de realizar los cambios:

1. Compilar el proyecto:
   ```bash
   ./mvnw clean compile
   ```

2. Ejecutar la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```

### 5. Acceder a Swagger UI

Una vez que la aplicación esté ejecutándose, acceder a Swagger UI desde el navegador:

- **URL principal:** `http://localhost:8080/swagger-ui/index.html`
- **URL alternativa:** `http://localhost:8080/swagger-ui.html`

## Funcionalidades de Swagger UI

### Explorar Endpoints
- Todos los endpoints REST de la aplicación estarán listados automáticamente
- Cada endpoint muestra:
  - Método HTTP (GET, POST, PUT, DELETE)
  - Ruta del endpoint
  - Parámetros requeridos
  - Ejemplos de respuestas

### Probar Endpoints
- Interfaz interactiva para enviar requests
- Campos para ingresar parámetros
- Botones para ejecutar requests
- Visualización de respuestas en tiempo real

### Documentación Automática
- Genera documentación técnica automáticamente
- Soporta autenticación JWT
- Muestra modelos de datos (DTOs)
- Incluye ejemplos de uso

## Mejoras Opcionales

### Anotaciones en Controladores

Para mejorar la documentación, se pueden agregar anotaciones específicas:

```java
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Endpoints para autenticación de usuarios")
public class AuthenticationController {

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario", description = "Registra un nuevo usuario en el sistema")
    @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de registro inválidos")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        // implementación
    }
}
```

### Configuración de Autenticación

Para probar endpoints protegidos desde Swagger UI, se puede configurar autenticación JWT:

```java
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("El Buen Corte API")
                        .description("API REST para el sistema de gestión de peluquería El Buen Corte")
                        .version("1.0.0"))
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
```

## Solución de Problemas

### Error de Compilación
- Verificar que la versión de SpringDoc sea compatible con Spring Boot 3.x
- Asegurarse de que todas las dependencias estén correctamente declaradas

### Acceso Denegado a Swagger UI
- Verificar que las rutas `/swagger-ui/**`, `/v3/api-docs/**` estén permitidas en la configuración de seguridad
- Confirmar que la aplicación esté ejecutándose en el puerto correcto

### Endpoints No Aparecen
- Verificar que los controladores estén anotados correctamente con `@RestController`
- Asegurarse de que los paquetes estén incluidos en el escaneo de componentes

## Conclusión

La implementación de Swagger/OpenAPI proporciona una documentación interactiva y profesional para la API de El Buen Corte. Facilita el desarrollo, testing y mantenimiento de la aplicación al proporcionar una interfaz clara para todos los endpoints disponibles.

Para más información, consultar la documentación oficial de SpringDoc OpenAPI: https://springdoc.org/

--

# Implementar Swagger en el proyecto el_buen_corte_backend

## Tareas Completadas

### 1. ✅ Agregar dependencia de SpringDoc OpenAPI al pom.xml

- Agregado `springdoc-openapi-starter-webmvc-ui` versión 2.6.0

### 2. ✅ Crear configuración de OpenAPI

- Creada clase `OpenApiConfig.java` en el paquete `config`
- Configurada información básica de la API (título, descripción, versión, contacto)

### 3. ✅ Actualizar configuración de seguridad

- Modificado `SecurityConfiguration.java` para permitir acceso público a:
  - `/swagger-ui/**`
  - `/v3/api-docs/**`
  - `/swagger-ui.html`

### 4. ✅ Verificar implementación

- Compilación exitosa del proyecto
- La aplicación puede ejecutarse y Swagger UI estará disponible

## Próximos pasos

### 5. Ejecutar la aplicación

- Ejecutar `mvn spring-boot:run` o `./mvnw spring-boot:run`
- Acceder a Swagger UI en `http://localhost:8080/swagger-ui/index.html`
- Verificar que todos los endpoints estén documentados

### 6. Mejoras opcionales

- Agregar anotaciones `@Operation` y `@ApiResponse` a los controladores para mejor documentación
- Configurar grupos de API si es necesario

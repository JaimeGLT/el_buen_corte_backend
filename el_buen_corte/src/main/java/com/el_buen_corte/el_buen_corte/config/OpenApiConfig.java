package com.el_buen_corte.el_buen_corte.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import java.util.List;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {

                // Definir esquema global BearerAuth
                SecurityScheme bearerAuthScheme = new SecurityScheme()
                                .name("JWT Auth")
                                .description("Introduce tu token JWT en el formato: Bearer eyJhbGciOiJIUzI1NiJ9...")
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .type(SecurityScheme.Type.HTTP);

                return new OpenAPI()

                                // 🔐 Requerir seguridad globalmente
                                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))

                                // 🔒 Registrar esquema bearer
                                .components(new Components()
                                                .addSecuritySchemes("bearerAuth", bearerAuthScheme))

                                // 🌍 Definir servidores (local + producción + testing)
                                .servers(List.of(
                                                new Server()
                                                                .url("http://localhost:8080")
                                                                .description("Servidor local"),
                                                new Server()
                                                                .url("https://el-buen-corte-server.onrender.com")
                                                                .description("Servidor producción"),
                                                new Server()
                                                                .url("https://staging.elbuencorte.com")
                                                                .description("Servidor staging (QA)")))

                                // 📝 Metadata de la API
                                .info(new Info()
                                                .title("El Buen Corte API")
                                                .description("""
                                                                API REST para el sistema de gestión de peluquería El Buen Corte.
                                                                Usa JWT para autenticación. Inicia sesión para obtener tu token.
                                                                """)
                                                .version("1.1.0")
                                                .contact(new Contact()
                                                                .name("Equipo de Desarrollo")
                                                                .email("soporte@elbuencorte.com")
                                                                .url("https://elbuencorte.com")));
        }
}

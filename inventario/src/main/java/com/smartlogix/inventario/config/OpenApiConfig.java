package com.smartlogix.inventario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

/**
 * Configuración de la documentación OpenAPI / Swagger UI del MS-Inventario.
 * Swagger UI disponible en: http://localhost:8085/swagger-ui.html
 * Especificación OpenAPI (JSON) en: http://localhost:8085/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI inventarioOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SmartLogix - MS Inventario API")
                        .description("Microservicio de gestión del catálogo de productos y niveles de stock.")
                        .version("1.0.0")
                        .contact(new Contact().name("Equipo SmartLogix")));
    }
}

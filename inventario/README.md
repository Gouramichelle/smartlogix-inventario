# SmartLogix · MS-Inventario

Microservicio de **gestión del catálogo de productos y control de stock** de la plataforma
SmartLogix. Forma parte de la arquitectura de microservicios del proyecto (Desarrollo
Fullstack III · DSY1106).

## Tecnologías
- Java 21 · Spring Boot 4.0.6 · Spring Data JPA (Hibernate)
- MySQL 9.6 (esquema `db_inventario`)
- Resilience4j (Circuit Breaker) · SpringDoc OpenAPI (Swagger)
- Pruebas: JUnit 5 + Mockito · Cobertura: JaCoCo

## Configuración
| Parámetro | Valor |
|-----------|-------|
| Puerto | `8085` |
| Base de datos | `jdbc:mysql://localhost:3306/db_inventario` (se crea automáticamente) |
| Usuario / clave | `root` / *(sin clave)* |

La configuración está en `src/main/resources/application.properties`. El esquema se crea
solo con `spring.jpa.hibernate.ddl-auto=update`.

## API REST
Base: `http://localhost:8085/api/inventario/productos`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/` | Lista todos los productos |
| GET | `/sku/{sku}` | Obtiene un producto por su SKU |
| POST | `/` | Crea un producto |
| PUT | `/{id}` | Actualiza un producto |
| DELETE | `/{id}` | Elimina un producto |

**Documentación interactiva (Swagger UI):** `http://localhost:8085/swagger-ui.html`
**Especificación OpenAPI (JSON):** `http://localhost:8085/v3/api-docs`

## Cómo instalar y ejecutar
Requisitos: Java 21, Maven (incluido vía `mvnw`) y MySQL en ejecución.

```bash
cd inventario
./mvnw spring-boot:run
```

## Cómo probar y ver la cobertura
```bash
./mvnw test
# Reporte de cobertura: target/site/jacoco/index.html
```

Cobertura actual: **~95%** de instrucciones (25 pruebas: servicio, controlador y contexto).

## Patrones aplicados
- **Repository** (Spring Data JPA): acceso a datos desacoplado.
- **Database-per-Service**: esquema MySQL propio e independiente.
- **Circuit Breaker** (Resilience4j): resiliencia ante fallos.

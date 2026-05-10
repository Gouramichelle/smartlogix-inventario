# SmartLogix Inventario (Microservicio de Inventario)

## Descripción
El **Microservicio de Inventario** es responsable de gestionar el catálogo de productos de SmartLogix. Maneja todas las operaciones CRUD relacionadas con los productos, incluyendo:

- Gestión del stock de productos
- Información de precios y descripciones
- Búsqueda de productos por SKU
- Actualización de inventario

## Tecnologías Utilizadas
- **Java 21**
- **Spring Boot 4.0.6**
- **Spring Data JPA** para persistencia
- **MySQL** como base de datos
- **Maven** como gestor de dependencias

## Cómo Ejecutar

### Prerrequisitos
- Java 21 instalado
- Maven instalado
- MySQL ejecutándose en localhost:3306
- Usuario de BD: `root` (sin contraseña por defecto)

### Pasos para Ejecutar
1. Navega al directorio del proyecto:
   ```bash
   cd smartlogix-inventario/inventario
   ```

2. Ejecuta la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```

3. La aplicación se ejecutará en: `http://localhost:8085`

### Configuración de Base de Datos
- **URL**: `jdbc:mysql://localhost:3306/db_inventario?createDatabaseIfNotExist=true`
- **Usuario**: `root`
- **Contraseña**: (vacía)
- **DDL Auto**: `update` (crea tablas automáticamente)

## Endpoints Principales
- `GET /api/inventario/productos` - Lista todos los productos
- `GET /api/inventario/productos/sku/{sku}` - Busca producto por SKU
- `POST /api/inventario/productos` - Crear nuevo producto
- `PUT /api/inventario/productos/{id}` - Actualizar producto existente
- `DELETE /api/inventario/productos/{id}` - Eliminar producto

## Modelo de Datos
### Producto
- `id`: Long (autogenerado)
- `nombre`: String
- `sku`: String (único)
- `descripcion`: String
- `precio`: Double
- `stock`: Integer

## Arquitectura
Este microservicio sigue los principios de Domain-Driven Design, con separación clara entre capas de controlador, servicio y repositorio. Utiliza JPA para el mapeo objeto-relacional con MySQL.
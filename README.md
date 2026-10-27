# AgroSierraGonzalesBackendd

Backend REST para el caso AgroSierra del examen de Desarrollo de Aplicaciones Móviles. Java 21, Spring Boot, Spring Security, JWT y MySQL.

## Requisitos
- Java 21
- Docker Desktop con Docker Compose

## Inicio rápido
En Windows:
1. docker compose up -d mysql
2. mvnw.cmd spring-boot:run

Linux/macOS: docker compose up -d mysql && ./mvnw spring-boot:run

El backend queda disponible en http://localhost:8080; MySQL en localhost:3307. Hibernate crea y actualiza las tablas automáticamente al iniciar. La primera ejecución agrega usuarios y datos semilla si la base está vacía.

## Usuarios de demostración
Todos usan Agro2026*:
- tecnico1 (TECNICO)
- jefe1 (JEFE_CAMPO)
- admin (ADMINISTRADOR)

## API v1
- POST /api/v1/auth/login
- GET /api/v1/lotes?pagina=0&tamanio=20&valle=Valle%20Alto&campaniaCerrada=false
- GET /api/v1/lotes/{id}
- GET /api/v1/lotes/{id}/labores?pagina=0&tamanio=20
- POST /api/v1/labores
- PUT /api/v1/labores/{id}
- DELETE /api/v1/labores/{id}
- POST /api/v1/lotes/{id}/cerrar-campania
- GET /api/v1/usuarios/me

Para rutas protegidas, enviar Authorization: Bearer <token>. Los listados usan contenido, pagina, tamanio, totalElementos, totalPaginas y ultima. Las reglas RN-01 a RN-04 viven en AgroService.

## Configuración
Se pueden cambiar credenciales y puertos con DB_URL, DB_USERNAME, DB_PASSWORD, JWT_SECRET, MYSQL_PORT, MYSQL_PASSWORD y MYSQL_ROOT_PASSWORD. Para despliegues reales, definir un secreto JWT y credenciales propios.

## Esquema
Paquetes: config (JWT, seguridad, datos semilla), dominio, repositorio, seguridad, servicio y web (controladores y DTO). spring.jpa.hibernate.ddl-auto=update crea/actualiza el esquema al iniciar.

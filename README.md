# AgroSierraGonzalesBackendd

Backend REST del caso AgroSierra. Java 21, Spring Boot 3.3, Spring Security, JWT y MySQL 8.

## Inicio rápido (Windows)
1. docker compose up -d mysql
2. mvnw.cmd spring-boot:run

La API queda en http://localhost:8080 y MySQL en localhost:3307. Hibernate crea/actualiza las tablas al iniciar. La primera ejecución carga 8 lotes, 20 labores y tres usuarios si las tablas están vacías.

## Usuarios de demostración
Contraseña para los tres: Agro2026*
- tecnico1 — TECNICO
- jefe1 — JEFE_CAMPO
- admin — ADMINISTRADOR

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

En rutas protegidas, enviar Authorization: Bearer <token>. Respuestas paginadas y errores siguen los DTO del paquete dto.

## Arquitectura de paquetes
- config: configuración Spring Security y datos semilla.
- controller: controladores REST y manejador común de errores.
- dto: peticiones, respuestas y paginación.
- entity / enums: entidades JPA y tipos del dominio.
- exception: errores de negocio.
- mapper: conversión de entidades a DTO.
- repository: repositorios Spring Data JPA.
- security: JWT, filtro, privilegios por rol y respuestas de seguridad.
- service / service.impl: contrato y reglas de negocio RN-01 a RN-04.
- util: límites reutilizables para paginación.

## IntelliJ IDEA
Abrir la carpeta raíz que contiene pom.xml. Si el editor muestra los imports de Spring en rojo, abrir pom.xml y elegir Add as Maven Project; luego, en la ventana Maven, usar Reload All Maven Projects. Verificar Project SDK = 21 y recargar el proyecto Maven.


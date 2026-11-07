# Trabajo Integrador — Ingeniería Web III

Backend académico para gestionar órdenes de carga de gas líquido e integrar, mediante APIs REST, los procesos de SAP, TMS y el sistema de carga.

## Stack y requisitos

- Java; el entorno usado para las pruebas fue Java 21. El `pom.xml` declara `java.version` 17.
- Spring Boot 4.1.1, Spring Web MVC, Spring Data JPA e Hibernate.
- H2 en memoria como base configurada para ejecución local.
- Maven Wrapper y Springdoc OpenAPI 3.2.0.

No se requiere instalar Maven por separado.

## Ejecución local

Desde la raíz del repositorio:

```bash
./mvnw spring-boot:run
```

En Windows también se puede usar `mvnw.cmd spring-boot:run`.

> `spring-boot:run` es el goal de Maven para iniciar la aplicación.

## Tests

```bash
./mvnw test
```

Resultado validado: **8 tests, 0 fallos y 0 errores**.

## OpenAPI / Swagger

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- Especificación OpenAPI: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Postman

La colección exportable está en `postman/IW3 - Circuito integral.postman_collection.json`. En Postman, importá ese archivo desde **Import** y ejecutá la colección completa con **Collection Runner**, respetando el orden de requests. Usa variables de colección (`baseUrl`, `numeroOrden` y `password`), por lo que no requiere un environment adicional.

El circuito simula SAP (recepción de la orden), TMS (tara y password), el sistema de carga (activación, mediciones y cierre), TMS (pesaje final) y la consulta de conciliación. La ejecución validada con Postman Runner obtuvo **21 PASS, 0 fallos y 0 errores**.

## APIs disponibles

Todas las operaciones pertenecen a `/api/v1/orders`; las rutas con `{numeroOrden}` usan el número externo de integración.

| Método | Ruta | Operación |
|---|---|---|
| `POST` | `/api/v1/orders` | Recibir una orden externa |
| `POST` | `/api/v1/orders/initial-weight` | Registrar pesaje inicial y generar password |
| `POST` | `/api/v1/orders/{numeroOrden}/activate` | Activar la carga |
| `POST` | `/api/v1/orders/{numeroOrden}/measurements` | Registrar una medición |
| `POST` | `/api/v1/orders/{numeroOrden}/close` | Cerrar la carga |
| `POST` | `/api/v1/orders/final-weight` | Registrar pesaje final |
| `GET` | `/api/v1/orders/{numeroOrden}/conciliation` | Consultar conciliación |

## Estructura relevante

```text
src/       Código y tests del backend
postman/   Colección y guía de ejecución
pom.xml    Configuración Maven y dependencias
mvnw       Maven Wrapper para Unix
mvnw.cmd   Maven Wrapper para Windows
```

---

Proyecto académico — Ingeniería Web III, IUA, 2026.

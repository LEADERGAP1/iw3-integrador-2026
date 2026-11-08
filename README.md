# Trabajo Integrador — Ingeniería Web III

Backend académico para gestionar órdenes de carga de gas líquido e integrar, mediante APIs REST, los procesos de SAP, TMS y el sistema de carga.

## Stack

- Java 17.
- Spring Boot 4.1.1.
- Spring Web MVC.
- Spring Data JPA / Hibernate.
- H2 para ejecución y tests locales.
- MySQL 8.0.36 para el entorno desplegado.
- Maven Wrapper.
- Springdoc OpenAPI 3.2.0.
- Docker y Docker Compose.
- Nginx como reverse proxy.
- HTTPS mediante Let's Encrypt.

No es necesario instalar Maven por separado.

## Ejecución local

Desde la raíz del repositorio:

```bash
./mvnw spring-boot:run
```

En Windows:

```bash
mvnw.cmd spring-boot:run
```

La configuración local utiliza una base H2 en memoria.

## Tests

```bash
./mvnw test
```

Resultado validado:

```text
Tests run: 8
Failures: 0
Errors: 0
Skipped: 0
```

## OpenAPI / Swagger

### Entorno local

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI: `http://localhost:8080/v3/api-docs`

### Entorno desplegado

URL pública:

`https://gabygauna-iw3.duckdns.org`

Al ingresar al dominio raíz se redirige automáticamente a Swagger.

- Swagger UI: `https://gabygauna-iw3.duckdns.org/swagger-ui/index.html`
- OpenAPI: `https://gabygauna-iw3.duckdns.org/v3/api-docs`

## APIs disponibles

Todas las operaciones pertenecen a `/api/v1/orders`.

Las rutas con `{numeroOrden}` utilizan el número externo de integración de la orden.

| Método | Ruta | Operación |
|---|---|---|
| `POST` | `/api/v1/orders` | Recibir una orden externa |
| `POST` | `/api/v1/orders/initial-weight` | Registrar pesaje inicial y generar password |
| `POST` | `/api/v1/orders/{numeroOrden}/activate` | Activar la carga |
| `POST` | `/api/v1/orders/{numeroOrden}/measurements` | Registrar una medición |
| `POST` | `/api/v1/orders/{numeroOrden}/close` | Cerrar la carga |
| `POST` | `/api/v1/orders/final-weight` | Registrar pesaje final |
| `GET` | `/api/v1/orders/{numeroOrden}/conciliation` | Consultar conciliación |

## Circuito implementado

El circuito integral simula la interacción entre los distintos sistemas externos:

1. SAP registra una nueva orden de carga.
2. TMS registra la tara inicial y se genera una password.
3. El sistema de carga valida la password y activa la operación.
4. Se reciben mediciones acumuladas de masa, densidad, temperatura y caudal.
5. Se valida que el caudal y la masa sean positivos y que la masa acumulada no disminuya.
6. El sistema de carga cierra la operación.
7. TMS registra el pesaje final.
8. La orden pasa al estado final.
9. Se consulta la conciliación entre los valores registrados.

## Postman

La colección se encuentra en:

```text
postman/IW3 - Circuito integral.postman_collection.json
```

Utiliza las siguientes variables de colección:

- `baseUrl`
- `numeroOrden`
- `password`

Puede importarse directamente desde Postman y ejecutarse mediante Collection Runner.

La colección incluye tanto el circuito principal como casos de error de las mediciones.

### Validación realizada

El circuito completo fue ejecutado contra el entorno público mediante HTTPS:

```text
Requests:   11
Assertions: 21
Failed:      0
```

Se validaron, entre otros:

- recepción de la orden;
- tara inicial;
- generación y captura de password;
- activación de la carga;
- mediciones válidas;
- rechazo de caudal igual a cero;
- rechazo de masa igual a cero;
- rechazo de masa acumulada decreciente;
- cierre de carga;
- pesaje final;
- transición a estado 4;
- conciliación final.

## Ejecución de Newman mediante Docker

No es necesario instalar Node.js ni Newman en el host.

Desde la raíz del repositorio:

```bash
docker run --rm \
  -v "$PWD/postman:/etc/newman" \
  postman/newman:alpine \
  run "/etc/newman/IW3 - Circuito integral.postman_collection.json" \
  --env-var "baseUrl=https://gabygauna-iw3.duckdns.org"
```

El resultado esperado es:

```text
requests:   11 executed / 0 failed
assertions: 21 executed / 0 failed
```

## Arquitectura de despliegue

```text
Cliente / Postman / Swagger
            |
          HTTPS
            |
gabygauna-iw3.duckdns.org
            |
          Nginx
            |
     127.0.0.1:8081
            |
  iw3-integrador-backend
     Spring Boot / Java 17
            |
  iw3-integrador-mysql
       MySQL 8.0.36
```

El backend no se expone directamente a Internet.

Docker publica el servicio únicamente en:

```text
127.0.0.1:8081
```

Nginx recibe las conexiones públicas HTTPS y actúa como reverse proxy hacia el backend.

## Persistencia

El entorno desplegado utiliza MySQL 8.0.36.

La base utilizada por el integrador es:

```text
iw3_integrador_2026
```

Las tablas son administradas mediante JPA/Hibernate y los datos de MySQL se mantienen en un volumen persistente de Docker.

La ejecución local y los tests Maven utilizan H2 en memoria.

## Docker

El despliegue utiliza dos contenedores principales:

```text
iw3-integrador-backend
iw3-integrador-mysql
```

El contenedor MySQL dispone de healthcheck y el backend se inicia una vez que la base se encuentra disponible.

Las credenciales de producción se almacenan en un archivo `.env` dentro de la infraestructura de la VM y no se versionan en el repositorio.

## HTTPS

El dominio público utiliza Nginx y un certificado TLS emitido por Let's Encrypt.

Las peticiones HTTP son redirigidas automáticamente a HTTPS.

Spring Boot procesa los headers enviados por Nginx para que OpenAPI y Swagger generen correctamente URLs con esquema `https`.

## Deploy en la VM

La infraestructura del integrador se encuentra en:

```text
~/infra-integrador
```

El repositorio utilizado por la VM se encuentra en:

```text
~/infra/app-integrador
```

El despliegue se automatiza mediante:

```bash
cd ~/infra-integrador
./deploy-integrador.sh
```

El script realiza automáticamente:

```text
1. Verificación de la rama develop.
2. Verificación de que el working tree esté limpio.
3. git pull --ff-only origin develop.
4. Ejecución de tests Maven.
5. Generación del JAR.
6. Actualización y reconstrucción del contenedor backend.
7. Verificación de disponibilidad de OpenAPI por HTTPS.
8. Ejecución completa de la colección Postman mediante Newman.
9. Verificación del estado final de los contenedores.
```

El deploy solamente finaliza correctamente si las validaciones previas son exitosas.

## Estructura relevante

```text
src/       Código fuente y tests del backend
postman/   Colección Postman del circuito integral
pom.xml    Configuración Maven y dependencias
mvnw       Maven Wrapper para Unix
mvnw.cmd   Maven Wrapper para Windows
README.md  Documentación general del proyecto
```

## Rama de integración

El entorno desplegado utiliza la rama:

```text
develop
```

La VM verifica que el repositorio se encuentre en esta rama antes de iniciar un nuevo deploy.

---

Proyecto académico — Ingeniería Web III, IUA, 2026.

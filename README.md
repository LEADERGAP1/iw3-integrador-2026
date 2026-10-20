# Trabajo Integrador — Ingeniería Web III

**Ingeniería Informática | IUA | 2026**

## Descripción del proyecto

Este proyecto consiste en el desarrollo de un sistema de gestión de órdenes de carga de gas líquido, que permite administrar y registrar las diferentes etapas del proceso, desde la recepción de una orden hasta su finalización y conciliación.

El sistema se integrará conceptualmente con sistemas externos de gestión, pesaje y carga, simulando sus comunicaciones mediante Postman.

Se desarrollará una aplicación backend que expondrá una API REST para recibir, validar, procesar y almacenar la información.

## Objetivo

Implementar una solución que permita:

- Administrar órdenes de carga de gas líquido.
- Integrar información proveniente de sistemas externos.
- Registrar pesajes iniciales y finales.
- Procesar mediciones de carga.
- Gestionar estados y transiciones de las órdenes.
- Mantener la trazabilidad de las operaciones.
- Obtener los resultados de conciliación de cada carga finalizada.

## Tecnologías previstas

| Tecnología | Utilización |
|---|---|
| Java | Lenguaje principal del backend |
| Spring Boot | Desarrollo de la aplicación y API REST |
| Maven | Gestión de dependencias y compilación |
| JPA / Hibernate | Persistencia de entidades |
| Base de datos relacional | Almacenamiento de información |
| OpenAPI / Swagger | Documentación de endpoints |
| Postman | Simulación de sistemas externos y pruebas |
| Git / GitHub | Control de versiones y colaboración |
| Docker / Docker Compose | Contenedorización y despliegue |
| Linux / VM | Entorno de ejecución del proyecto |
| Nginx | Proxy inverso, sujeto a la configuración de la VM |

Las versiones específicas y la configuración definitiva se establecerán durante la preparación del entorno.

## Funcionalidades principales

### 1. Recepción de órdenes

Recepción de datos de órdenes desde un sistema externo simulado, incluyendo información del camión, chofer, cliente, producto, turno de carga y cantidad programada.

La orden se crea en estado **Pendiente de pesaje inicial**.

### 2. Registro de pesaje inicial

Registro de la tara del camión, generación de una contraseña de activación de cinco dígitos y actualización del estado de la orden.

### 3. Proceso de carga

Validación de la contraseña de activación y entrega del preset al sistema de carga.

Recepción de mediciones de masa acumulada, densidad, temperatura y caudal, con validación y persistencia de los datos correspondientes.

### 4. Cierre de la orden

Finalización del proceso de carga, bloqueo de nuevas mediciones y actualización del estado de la orden.

### 5. Pesaje final y conciliación

Registro del pesaje final y cálculo de los resultados de conciliación entre la masa registrada por la balanza y la informada por el sistema de carga.

## Estados de una orden

El sistema contempla cuatro estados secuenciales:

1. **Pendiente de pesaje inicial**
2. **Con pesaje inicial registrado**
3. **Cerrada para carga**
4. **Finalizada**

Las transiciones deberán respetar el orden establecido y conservar información para auditoría.

## Arquitectura propuesta

Se utilizará una aplicación backend Spring Boot con una API REST, organizada en capas para separar responsabilidades:

- **Controllers:** recepción y respuesta de solicitudes HTTP.
- **Services:** lógica y reglas de negocio.
- **Repositories:** acceso y persistencia de datos.
- **Entities:** representación del modelo de dominio.
- **DTOs:** intercambio de información entre las APIs y sus consumidores.

Los sistemas externos SAP, TMS y el sistema de carga se simularán mediante solicitudes HTTP realizadas desde Postman.

## Equipo de desarrollo

| Integrante | Responsabilidad principal |
|---|---|
| Giuliano | Modelo de datos, entidades, recepción de órdenes y pesaje inicial |
| Cachenche | Activación, mediciones, cierre y conciliación |
| Gabriel | Integración, infraestructura, despliegue, Swagger y Postman |

Todos los integrantes participarán en la revisión, integración y comprensión del sistema completo.

## Entregables del segundo parcial

- [ ] Implementación de las APIs correspondientes a los cinco procesos.
- [ ] Documentación OpenAPI / Swagger.
- [ ] Colección Postman para probar y validar las APIs.
- [ ] Registro y validación de estados y transiciones.
- [ ] Integración y pruebas del circuito completo.
- [ ] Preparación de la demostración y defensa individual.

**Primera entrega:** 10 de noviembre de 2026.

## Alcance posterior — Examen final

Para la entrega final se incorporarán las funcionalidades adicionales exigidas por el profesor:

- Configuración de frecuencia de almacenamiento de mediciones.
- Cálculo de promedios para conciliación.
- Seguridad y roles de usuario.
- Alarmas configurables de temperatura y notificaciones por correo.
- Interfaz web con autenticación.
- Monitoreo de órdenes en tiempo real.
- ETA y tiempo transcurrido de carga.
- Visualización y aceptación de alarmas.
- Consulta de conciliaciones desde la interfaz gráfica.

## Estado del proyecto

**En preparación.**

Actualmente se está organizando el repositorio, definiendo las responsabilidades del equipo y analizando el diseño de la solución.

Las funcionalidades se irán registrando a medida que sean desarrolladas y verificadas.

## Documentación

La documentación técnica del proyecto incluirá:

- Análisis de requisitos.
- Diseño de arquitectura.
- Modelo de datos.
- Contratos de endpoints REST.
- Decisiones técnicas y justificaciones.
- Configuración de infraestructura.
- Pruebas funcionales y de integración.
- Registro de problemas y soluciones.

Esta documentación servirá también como material de estudio para la defensa técnica individual.

---

**Proyecto académico — Ingeniería Web III, IUA — 2026.**

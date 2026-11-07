# Colección Postman: circuito integral IW3

## Importación y ejecución

1. Iniciá el backend en `http://localhost:8080`.
2. Importá `IW3 - Circuito integral.postman_collection.json` en Postman.
3. Abrí **Collection Runner**, seleccioná la colección completa y ejecutala una vez, respetando el orden de carpetas y requests.

La colección no necesita environment: `baseUrl`, `numeroOrden` y `password` son variables de colección. El primer request crea un `numeroOrden` numérico nuevo para cada ejecución, evita colisiones con órdenes ya registradas, y limpia la password anterior. El endpoint de pesaje inicial guarda automáticamente la password que devuelve; activación la reutiliza.

Todos los datos son ficticios. Los códigos maestros (`PM-CAM-001`, `PM-CHO-001`, `PM-CLI-001`, `PM-PROD-001`) son de prueba; la lógica del backend los sincroniza por código externo. Se usan valores positivos para tara, masas, caudales y pesaje final. La masa queda por debajo del preset de 8000 kg.

## Secuencia

1. SAP recibe la orden y la crea en estado 1.
2. TMS registra la tara; se captura `password` y la orden pasa a estado 2.
3. El sistema de carga activa la orden y envía dos mediciones válidas con masa creciente.
4. La carpeta `90 - Casos de error - mediciones` se ejecuta antes del cierre: verifica el rechazo de caudal cero, masa cero y masa decreciente. Cada caso espera HTTP 400.
5. Se cierra la carga (estado 3) y TMS registra el peso final (estado 4).
6. El cliente consulta la conciliación y verifica sus campos y valores principales.

## Variables de colección

| Variable | Uso |
| --- | --- |
| `baseUrl` | URL base del backend; por defecto `http://localhost:8080`. |
| `numeroOrden` | Número externo generado por el pre-request de SAP para cada ejecución. |
| `password` | Password capturada de la respuesta del pesaje inicial y utilizada para activar. |

No se almacenan credenciales ni secretos reales. Para apuntar a otra instancia, cambiá `baseUrl` en las variables de colección.

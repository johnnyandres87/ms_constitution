
Microservicio de ms constitution con capacidad de conectarse a colas de mensajes.

## Configuración

Para configurar el microservicio se debe tener desplegado una instancia de locastack e ibmmq para la conexión a las colas de mensajes.
para este despliegue puedes valerte de docker o cualquier otro contenedor de tu preferencia.

### Configuración de ibmmq

```bash
docker run -e LICENSE=accept -e MQ_QMGR_NAME=QM1 -p 1414:1414 -p 9443:9443 -d --name ibmmq -e MQ_APP_PASSWORD=passw0rd -e MQ_ADMIN_PASSWORD=passw0rd ibmcom/mq
```
para acceder a la consola de ibmmq puedes acceder a la siguiente url: https://localhost:9443/ibmmq/console/

### Configuracion de secreto para ibmmq

```bash
aws secretsmanager create-secret  --name rabbit-secret --secret-string file://secreto.json  --endpoint-url=http://localhost:4566
```
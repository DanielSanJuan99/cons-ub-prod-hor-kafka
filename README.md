# cons-ub-prod-hor-kafka

Microservicio procesador que consume ubicaciones GPS de vehículos desde Kafka, estima horarios de llegada a paradas cercanas y publica los resultados en un segundo topic. Actúa como eslabón intermedio en la cadena de procesamiento de datos de transporte público.

## Tecnologías

- Java 21
- Spring Boot 3.3.1
- Spring Kafka
- Apache Kafka (1 broker — optimizado para despliegue en EC2)
- Docker (imagen multi-plataforma: linux/amd64, linux/arm64)

## Puerto

```
8082
```

## Topics Kafka

| Dirección | Topic | Consumer Group | Particiones | Réplicas |
|-----------|-------|----------------|-------------|----------|
| Consume   | `ubicaciones_vehiculos` | `procesamiento-ubicaciones-group` | 3 | 1 |
| Produce   | `horarios` | — | 3 | 1 |

## Lógica de procesamiento

Por cada ubicación recibida:

1. Selecciona una parada aleatoria de 8 posibles (Terminal, Plaza Central, Hospital, Universidad, Mall, Metro, Estadio, Parque)
2. Calcula hora de llegada = hora actual + 1–15 minutos aleatorios (zona `America/Santiago`)
3. Calcula hora estimada de salida = hora de llegada + 2 minutos
4. Publica el horario resultante al topic `horarios`

## Endpoints REST

Base: `/api/horarios`

| Método | Path | Descripción |
|--------|------|-------------|
| `POST` | `/api/horarios` | Envía un horario manual a Kafka |
| `POST` | `/api/horarios/listener/{id}/pausar` | Pausa el listener Kafka por ID |
| `POST` | `/api/horarios/listener/{id}/reanudar` | Reanuda el listener Kafka por ID |
| `GET`  | `/api/horarios/listener/{id}/estado` | Estado del listener (pausado/activo) |
| `GET`  | `/api/horarios/health` | Health check del servicio |

## Estructura del mensaje producido

```json
{
  "id": 1,
  "vehiculoId": "BUS-001",
  "ruta": "Ruta 101 - Providencia",
  "paradaNombre": "Metro",
  "horaLlegada": "14:35:00",
  "horaEstimadaSalida": "14:37:00",
  "latitud": -33.4512,
  "longitud": -70.6693,
  "timestamp": "2026-03-02T03:14:00Z"
}
```

## Ejecución

### Con Docker Compose (recomendado)

Desde el directorio raíz del proyecto:

```bash
docker compose build cons-ub-prod-hor-kafka
docker compose up -d cons-ub-prod-hor-kafka
```

### Con Docker directamente

```bash
docker build -t cons-ub-prod-hor-kafka .
docker run -p 8082:8082 \
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka-1:9092 \
  --network kafka-net \
  cons-ub-prod-hor-kafka
```

### Imagen Docker Hub

```bash
docker pull dimmox/cons-ub-prod-hor-kafka:latest
```

### Local con Maven

```bash
./mvnw spring-boot:run
```

Requiere Kafka corriendo en `localhost:29092`.

## Variables de entorno

| Variable | Descripción | Valor por defecto |
|----------|-------------|-------------------|
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | Brokers Kafka | `kafka-1:9092` |
| `SERVER_PORT` | Puerto del servicio | `8082` |

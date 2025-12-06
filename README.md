# aviation-service
Spring Boot microservice for retrieving aviation information from the Aviation API.
(**Mainly** : retrieving airport information using ICAO codes from the Aviation API. as per assessment request)

### Requirements
- Java 17
- Gradle wrapper (included)
- Docker (optional to containerize)
- JMeter (optional for load testing)



## Architecture Overview
![img.png](docs/arch%20overview.png)

## AviationAPI documentation
- Airports : https://docs.aviationapi.com/#tag/airports
- Example: https://api.aviationapi.com/v1/airports?apt=KATL

## Architecture Decisions

### 1. **Layered Architecture**
- **Controller Layer**: HTTP request handling and validation
- **Service Layer**: Business logic and orchestration
- **Client Layer**: External API integration with resilience patterns
- **Clean separation** enables independent testing and scaling

### 2. **Resilience Patterns**
- **Circuit Breaker**: Prevents cascading failures (50% threshold, 30s recovery)
- **Retry Logic**: 3 attempts with exponential backoff (2s base)
- **Rate Limiting**: 100 requests/minute to protect upstream API
- **Timeouts**: 5s connection/read timeouts
- **Fallback**: Returns cached/default data when circuit opens

### 3. **Scalability Features**
- **Stateless design**: No session state, horizontally scalable
- **Reactive WebClient**: Non-blocking I/O for better resource utilization
- **Connection pooling**: Efficient HTTP connection management
- **Graceful shutdown**: 30s drain period for in-flight requests

### 4. **Observability**
- **Structured logging**: SLF4J with contextual information
- **Metrics**: Prometheus-compatible metrics via Actuator
- **Health checks**: Liveness/readiness probes
- **Distributed tracing ready**: Can integrate Zipkin/Jaeger

### 5. **Security**
- **Input validation**: ICAO code format validation
- **Non-root container**: Docker runs as unprivileged user
- **Error sanitization**: No sensitive data in error responses

1. **Clone the repository**
```bash
git clone https://github.com/typear247/aviation-service
cd aviation-service
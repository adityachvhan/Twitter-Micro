# service-registry

Netflix Eureka Service Registry for all Twitter microservices.
Runs on **port 8761**.

---

## Purpose

Every microservice registers itself here on startup.
The API gateway discovers service locations via Eureka instead of using
hardcoded `localhost:PORT` URLs — enabling load balancing and dynamic scaling.

---

## Start order

```
1. service-registry   (start first — others depend on it)
2. auth-service
3. user-service
4. twit-service
5. like-service
6. payment-service
7. api-gateway        (start last)
```

---

## Run locally

```bash
cd service-registry
./mvnw spring-boot:run
```

Eureka Dashboard: http://localhost:8761

---

## Registered services (visible in dashboard after startup)

| Service | App name |
|---|---|
| auth-service | AUTH-SERVICE |
| user-service | USER-SERVICE |
| twit-service | TWIT-SERVICE |
| like-service | LIKE-SERVICE |
| payment-service | PAYMENT-SERVICE |
| api-gateway | API-GATEWAY |

---

## How each service registers

Each service has these entries in its `application.properties`:

```properties
spring.application.name=user-service
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.prefer-ip-address=true
```

And this dependency in its `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

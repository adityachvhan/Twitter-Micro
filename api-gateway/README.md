# api-gateway

Single entry point for all client requests. Runs on **port 8080**.

---

## Responsibilities

| Concern | Handled here? |
|---|---|
| JWT validation | Yes — validates token, extracts email |
| Route to correct service | Yes — via Eureka service discovery |
| Inject X-Auth-User-Email header | Yes — downstream services trust this |
| CORS for frontend | Yes |
| Block /internal/** externally | Yes — 403 Forbidden |
| Business logic | No |

---

## Request flow

```
Client
  └─> GET /api/twits/ + Authorization: Bearer <jwt>
        └─> api-gateway (port 8080)
              ├─ JwtAuthFilter validates JWT
              ├─ Extracts email from claims
              ├─ Injects X-Auth-User-Email: alice@example.com
              └─> twit-service (port 8083) via Eureka lb://TWIT-SERVICE
                    └─> Returns response to client
```

---

## Route table

| Path | Filter | Downstream |
|---|---|---|
| `/auth/**` | None (public) | AUTH-SERVICE |
| `/api/users/**` | JwtAuthFilter | USER-SERVICE |
| `/api/twits/**` | JwtAuthFilter | TWIT-SERVICE |
| `/api/likes/**` | JwtAuthFilter | LIKE-SERVICE |
| `/api/payment/subscribe/**` | JwtAuthFilter | PAYMENT-SERVICE |
| `/api/payment/verify` | None (Razorpay callback) | PAYMENT-SERVICE |
| `/internal/**` | BlockInternalFilter → 403 | Blocked |

---

## Prerequisites

- Java 17+, Maven 3.8+
- `service-registry` must be running on port 8761 first

---

## Run locally

```bash
# Start service-registry first
cd service-registry && ./mvnw spring-boot:run

# Then start gateway
cd api-gateway && ./mvnw spring-boot:run
```

---

## Startup order

```
1. service-registry  :8761
2. auth-service      :8081
3. user-service      :8082
4. twit-service      :8083
5. like-service      :8084
6. payment-service   :8085
7. api-gateway       :8080  ← last
```

# auth-service

Microservice responsible for user registration, login, and JWT issuance.
Runs on **port 8081**.

---

## Responsibilities

| Concern | Handled here? |
|---|---|
| Signup (email + password) | Yes |
| Signin + JWT generation | Yes |
| Password hashing (BCrypt) | Yes |
| JWT validation | **No** — API gateway |
| User profiles | **No** — user-service |
| Google OAuth | Partial — stores `loginWithGoogle` flag |

---

## Prerequisites

- Java 17+
- Maven 3.8+
- MySQL running locally with `auth_db` schema

```sql
CREATE DATABASE auth_db;
```

---

## Run locally

```bash
cd auth-service
./mvnw spring-boot:run
```

Swagger UI: http://localhost:8081/swagger-ui.html

---

## API endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/auth/signup` | Register a new user |
| POST | `/auth/signin` | Sign in, receive JWT |
| GET  | `/auth/health` | Liveness probe |

---

## Example requests

```bash
# Signup
curl -X POST http://localhost:8081/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"email":"alice@example.com","password":"secret123","fullName":"Alice","birthDate":"1995-06-15"}'

# Signin
curl -X POST http://localhost:8081/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"email":"alice@example.com","password":"secret123"}'
```

---

## How it fits in the architecture

```
Client
  └─> POST /auth/signup or /auth/signin
        └─> auth-service (port 8081)
              ├─> auth_db  (saves email + hashed password)
              └─> POST /internal/users ──> user-service (creates profile)
                    └─> Returns JWT to client

Client (subsequent requests)
  └─> Any /api/** endpoint
        └─> API gateway (port 8080)
              ├─> Validates JWT
              ├─> Injects X-Auth-User-Email header
              └─> Forwards to target service
```

---

## Running tests

```bash
./mvnw test
```

# ⛽ Gas Station API

REST API for managing a gas station's fuel types, pumps, and supply records. Built with Java 21 and Spring Boot 4, using soft-delete to preserve historical data.

---

## 🛠️ Technologies

| Technology              |
|-------------------------|
| Java 21                 |
| Spring Boot             |
| Spring Data JPA         | 
| Spring Validation       | 
| PostgreSQL              | 
| Flyway                  |
| Lombok                  | 
| JUnit 5 + Mockito       | 
| H2 (test)               |
| Docker / Docker Compose | 

---

## 📐 Architecture

```
src/main/java/com/jessicavieiradev/gasStation/
├── controller/      # REST endpoints
├── service/         # Business logic
├── repository/      # JPA repositories
├── domain/
│   ├── entity/      # JPA entities
│   └── enums/       # Status enum (ativo / inativo)
├── dto/             # Request and Response records
├── mapper/          # Entity <-> DTO conversion
└── exception/       # BusinessException + GlobalExceptionHandler
```

Entities use `@SQLDelete` and `@SQLRestriction` for soft-delete — records are never physically removed, just marked as `inativo`.

---

## 🗺️ API Routes

Base URL: `http://localhost:8080/api/v1`

### Fuel Types — `/fuel-types`

| Method | Endpoint | Description | Status |
|---|---|---|---|
| `POST` | `/fuel-types` | Create fuel type | `201` |
| `GET` | `/fuel-types` | List all active fuel types | `200` |
| `GET` | `/fuel-types/{id}` | Get fuel type by ID | `200` |
| `PUT` | `/fuel-types/{id}` | Update fuel type | `200` |
| `DELETE` | `/fuel-types/{id}` | Soft-delete fuel type | `204` |
| `PATCH` | `/fuel-types/{id}/reactivate` | Reactivate fuel type | `204` |

### Fuel Pumps — `/fuel-pumps`

| Method | Endpoint | Description | Status |
|---|---|---|---|
| `POST` | `/fuel-pumps` | Create fuel pump | `201` |
| `GET` | `/fuel-pumps` | List all active pumps | `200` |
| `GET` | `/fuel-pumps/{id}` | Get pump by ID | `200` |
| `PUT` | `/fuel-pumps/{id}` | Update pump | `200` |
| `DELETE` | `/fuel-pumps/{id}` | Soft-delete pump | `204` |
| `PATCH` | `/fuel-pumps/{id}/reactivate` | Reactivate pump | `204` |

> A pump cannot be reactivated if its associated fuel type is inactive.

### Fuel Supplies — `/fuel-supplies`

| Method | Endpoint | Description | Status |
|---|---|---|---|
| `POST` | `/fuel-supplies` | Register a supply | `201` |
| `GET` | `/fuel-supplies` | List all supplies | `200` |
| `GET` | `/fuel-supplies/{id}` | Get supply by ID | `200` |
| `DELETE` | `/fuel-supplies/{id}` | Soft-delete supply | `204` |

> When creating a supply, provide either `literage` **or** `totalAmount` — not both. The missing value is calculated automatically based on the fuel type price.

---

## 📦 Request / Response Examples

### POST `/api/v1/fuel-types`
```json
// Request
{
  "name": "Gasolina Comum",
  "price": 5.89
}

// Response 201
{
  "id": "uuid",
  "name": "Gasolina Comum",
  "price": 5.89,
  "status": "ativo",
  "createdAt": "2025-05-20T10:00:00"
}
```

### POST `/api/v1/fuel-pumps`
```json
// Request
{
  "name": "Bomba 01",
  "fuelTypeId": "uuid-of-fuel-type"
}
```

### POST `/api/v1/fuel-supplies`
```json
// Option 1 — provide literage, totalAmount is calculated
{
  "fuelPumpId": "uuid-of-pump",
  "literage": 20.0
}

// Option 2 — provide totalAmount, literage is calculated
{
  "fuelPumpId": "uuid-of-pump",
  "totalAmount": 100.00
}

// Optional field
{
  "fuelPumpId": "uuid-of-pump",
  "literage": 20.0,
  "date": "2025-05-20T14:30:00"  // defaults to now if omitted
}
```

---

## 🚀 Running the Project

### Prerequisites

- Docker and Docker Compose installed
- Git

### 1. Clone the repository

```bash
git clone https://github.com/jessicavieiradev/gasStationJava.git
cd gasStationJava
```

### 2. Configure environment variables

Copy the example file and fill in your values:

```bash
cp .env-example .env
```

> **Note:** `DB_URL` must use `postgres` as the host (Docker service name), not `localhost`.

### 3. Run with Docker Compose

```bash
docker compose up --build
```

The API will be available at `http://localhost:8080`. The database migrations run automatically via Flyway on startup.

### Running locally (without Docker)

Make sure you have Java 21 and a PostgreSQL instance running, then update `DB_URL` in your `.env` to point to `localhost`:

```bash
./mvnw spring-boot:run
```

---

## 🧪 Tests

The project has unit tests for services (Mockito) and integration tests for repositories with custom queries (`@DataJpaTest` + H2).

```bash
./mvnw test
```

Test structure:

```
src/test/
├── java/.../repository/
│   ├── FuelTypeRepositoryTest.java   # native query tests
│   └── FuelPumpRepositoryTest.java
├── service/
│   ├── FuelTypeServiceTest.java
│   ├── FuelPumpServiceTest.java
│   └── FuelSupplyServiceTest.java
└── resources/
    └── application-test.properties   # H2 in-memory config
```

---

## 🗄️ Database Migrations

Managed by Flyway, located at `src/main/resources/db/migration/`:

| File | Description |
|---|---|
| `V1__create_fuel_type.sql` | Creates `fuel_types` table |
| `V2__create_fuel_pump.sql` | Creates `fuel_pumps` table |
| `V3__create_fuel_supply.sql` | Creates `fuel_supplies` table |
| `V4__add_status_column_fuel_supply.sql` | Adds `status` column to supplies |

---

## 🔒 Soft Delete

All three entities use soft-delete via Hibernate annotations:

```java
@SQLDelete(sql = "UPDATE fuel_types SET status = 'inativo' WHERE id = ?")
@SQLRestriction("status = 'ativo'")
```

This means `DELETE` requests update the `status` column to `inativo` instead of removing the row. All standard queries automatically filter by `status = 'ativo'`. Records can be restored via the `/reactivate` endpoint.

---

## 📄 License

This project is for study purposes.

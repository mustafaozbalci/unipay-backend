# UniPay

UniPay is a closed-loop, campus-wide prepaid payment and parking management system.
Built with Spring Boot and PostgreSQL on the backend, it provides users with seamless campus café ordering, NFC-style parking entry/exit, balance top-up via Iyzico, and real-time parking area monitoring.

---

## Table of Contents

* [Features](#features)
* [Architecture & Tech Stack](#architecture--tech-stack)
* [Getting Started](#getting-started)

  * [Prerequisites](#prerequisites)
  * [Configuration](#configuration)
  * [Database Setup](#database-setup)
  * [Running the Application](#running-the-application)
* [API Reference](#api-reference)

  * [Authentication & Users](#authentication--users)
  * [Payments](#payments)
  * [Restaurants & Orders](#restaurants--orders)
  * [Parking Areas & Sessions](#parking-areas--sessions)
* [Scheduled Tasks & Initialization](#scheduled-tasks--initialization)
* [CORS & Security](#cors--security)
* [DataLoader (Sample Data)](#dataloader-sample-data)
* [License](#license)

---

## Features

* **User Management**

  * Registration & Login with JWT
  * Profile details, password & plate updates
* **Balance & Payments**

  * Prepaid wallet top-up via Iyzico sandbox
  * Automatic balance checks, refunds, credits
* **Café Ordering**

  * Browse restaurants, place orders
  * Restaurant role: view & update order status
  * Order history for users & restaurants
* **Parking Management**

  * Real-time dashboard of parking areas (Available / Full / Closed)
  * Vehicle entry/exit sessions with fee calculation
  * User parking history & admin overview
* **Scheduling**

  * Automatic open at 05:00 and close at 22:00 (Europe/Istanbul)

---

## Architecture & Tech Stack

* **Backend**: Java 17, Spring Boot, Spring Security (JWT), Spring Data JPA
* **Database**: PostgreSQL
* **Mapping & Validation**: MapStruct, Bean Validation (Jakarta)
* **Logging**: SLF4J / Logback
* **Payment Gateway**: Iyzico Java SDK (sandbox)
* **Build & Dependency Management**: Maven
* **Scheduling**: Spring `@Scheduled` with cron expressions

---

## Getting Started

### Prerequisites

* Java 17+
* Maven 3.6+
* PostgreSQL 12+

### Configuration

Fill in **src/main/resources/application.properties** (or use environment variables):

```properties
# Application
spring.application.name=Unipay

# PostgreSQL DataSource
spring.datasource.url=jdbc:postgresql://localhost:5432/Unipay
spring.datasource.username=postgres
spring.datasource.password=12345
spring.jpa.hibernate.ddl-auto=update

# CORS
app.allowed.origins=http://localhost:3000

# Iyzico (sandbox)
iyzico.apiKey=your-sandbox-api-key
iyzico.secretKey=your-sandbox-secret-key
iyzico.baseUrl=https://sandbox-api.iyzipay.com
```

### Database Setup

1. Create PostgreSQL database:

   ```sql
   CREATE DATABASE "Unipay";
   ```
2. Ensure the user and password in `application.properties` have privileges.

### Running the Application

```bash
# Clone repository
git clone https://github.com/mustafaozbalci/unipay-backend
cd unipay

# Build and run
mvn clean spring-boot:run
```

The backend will start on **[http://localhost:8080](http://localhost:8080)** by default.

---

## API Reference

### Authentication & Users

| Method | Endpoint                   | Description                  | Auth       |
| ------ | -------------------------- | ---------------------------- | ---------- |
| POST   | `/api/auth/register`       | Register a new user          | Public     |
| POST   | `/api/auth/login`          | Authenticate and receive JWT | Public     |
| GET    | `/api/auth/details`        | Get current user’s profile   | Bearer JWT |
| PUT    | `/api/auth/updatePassword` | Update password              | Bearer JWT |
| PUT    | `/api/auth/updatePlate`    | Update vehicle plate         | Bearer JWT |

### Payments

| Method | Endpoint               | Description              | Auth       |
| ------ | ---------------------- | ------------------------ | ---------- |
| POST   | `/api/payment/deposit` | Top-up wallet via Iyzico | Bearer JWT |

### Restaurants & Orders

| Method | Endpoint                        | Description                        | Auth (Role) |
| ------ | ------------------------------- | ---------------------------------- | ----------- |
| POST   | `/api/restaurants/list`         | List restaurant names              | Public      |
| POST   | `/api/restaurants/add`          | Add new restaurant                 | RESTAURANT  |
| POST   | `/api/restaurants/update/{id}`  | Rename existing restaurant         | RESTAURANT  |
| POST   | `/api/restaurants/delete/{id}`  | Delete a restaurant                | RESTAURANT  |
| POST   | `/api/orders/place`             | Place a new order                  | Bearer JWT  |
| POST   | `/api/orders/restaurant/orders` | Get orders for restaurant          | RESTAURANT  |
| GET    | `/api/orders/user/orders`       | Get order history for current user | Bearer JWT  |
| POST   | `/api/orders/update-status`     | Update order status & prep time    | RESTAURANT  |

### Parking Areas & Sessions

| Method | Endpoint                                  | Description                                 | Auth (Role) |
| ------ | ----------------------------------------- | ------------------------------------------- | ----------- |
| GET    | `/api/parking-areas`                      | Retrieve all parking areas                  | Bearer JWT  |
| PUT    | `/api/parking-areas/{id}`                 | Update status of an area                    | Bearer JWT  |
| POST   | `/api/parking/enter?parkingAreaId={id}`   | Vehicle entry; returns session DTO          | Bearer JWT  |
| POST   | `/api/parking/exit?sessionId={id}`        | Vehicle exit; returns completed session DTO | Public      |
| GET    | `/api/parking/history`                    | Get current user’s parking history          | Bearer JWT  |
| GET    | `/api/parking/admin/history`              | Get all sessions (ADMIN only)               | ADMIN       |
| GET    | `/api/parking/current-fee?sessionId={id}` | Get in-progress fee for session             | Public      |

---

## Scheduled Tasks & Initialization

* **Daily Close**: At 22:00 Europe/Istanbul, all areas set to `CLOSED`.
* **Daily Open**: At 05:00 Europe/Istanbul, all areas set to `AVAILABLE`.
* **On Startup**: Immediately sets areas to `CLOSED` if outside operating hours (05:00–22:00), otherwise `AVAILABLE`.

---

## CORS & Security

* **CORS** configured globally via `GlobalCorsConfig`, defaulting to `http://localhost:3000`.
* **Spring Security** uses JWT tokens, stateless sessions, and method-level `@PreAuthorize` checks.

---

## DataLoader (Sample Data)

On application startup, `DataLoader` populates:

* Sample restaurants (`nero`, `espressolab`).
* Service accounts (`otopark`, `mustafa`, `tolga`, `dogukan`) with default password `123456`.
* Preconfigured parking areas with varied statuses and example parking sessions.

---

## License

This project is licensed under the [MIT License](LICENSE).
Feel free to fork, modify, and contribute back via pull requests.

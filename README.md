# 🚀 Trading Platform – Microservices System

This project is a modular microservices-based trading platform, built using **Spring Boot**, **WebFlux**, **gRPC**, and **Reactive PostgreSQL/MongoDB** technologies.

Each service is independently deployable and communicates through REST, **gRPC**, and **Kafka** (optional in future).

---

## 🧩 Microservices Overview

| Service               | Description                                              | Docs / Details |
|-----------------------|----------------------------------------------------------|----------------|
| **Authorization Service** | Handles registration, login, JWT issuance and validation | [🔐 Auth Service](./authorization-service/README.md) |
| **Deal Service**         | Manages user accounts and portfolios via gRPC          | [💰 Deal Service](./deal-service/README.md) |
| **Stock Service**        | Provides real-time stock data, integrated with DB       | [📈 Stock Service](./stock-service/README.md) |
| **Config Server**        | Centralized configuration service (Spring Cloud Config) | [⚙️ Config Server](./config-server/README.md) |
| **Service Discovery**    | Eureka Server for registering and discovering services  | [🧭 Service Discovery](./service-discovery/README.md) |
| **Shared Context**       | Shared library for JWT parsing, context propagation etc. | [📦 Shared Context](./shared-context/README.md) |

---

## ⚙️ Tech Stack Summary

| Tech / Framework             | Description                                   |
|-----------------------------|-----------------------------------------------|
| **Java**                    | Version 21                                    |
| **Spring Boot**             | Microservices core framework                  |
| **Spring Cloud**            | Config, Discovery (Eureka), Dependency Mgmt   |
| **WebFlux (Reactive)**      | Reactive REST API layer                       |
| **gRPC + Protobuf**         | High-performance service communication        |
| **R2DBC (PostgreSQL)**      | Reactive database access                      |
| **MongoDB (Reactive)**      | Used in authorization-service                |
| **Flyway**                  | DB migrations                                 |
| **Lombok**                  | Code generation utilities                     |
| **JWT (JJWT)**              | Authentication tokens                         |

---

## 📂 Repository Structure

```bash
.
├── authorization-service/    # Handles user auth and JWT
├── deal-service/             # gRPC service for account management
├── stock-service/            # Stock data + real-time updates
├── config-server/            # Spring Cloud Config Server
├── service-discovery/        # Eureka Server
├── shared-context/           # Common library for token/context handling
├── docker-compose.yam/       # External dependencies
└── README.md                 # This file

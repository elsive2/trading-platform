
---

# ⚙️ Config Server

**Config Server** is a centralized configuration server for managing application settings across all microservices in the **Trading Platform** ecosystem. It provides versioned and dynamic configuration through Git or native file system.

---

## 🚀 Features

- 🧠 Centralized configuration management
- 🔁 Live refresh support via Spring Cloud Bus (if configured)
- 🗂️ Versioned property files (e.g., `application.yml`, `service-name.yml`)
- 🌐 Integration with:
    - `Eureka Discovery` for service registration
    - `Spring Cloud Config` for remote config delivery

---

## 🧱 Technologies

| Stack                    | Version   |
|--------------------------|-----------|
| Java                     | 21        |
| Spring Boot              | 3.4.4     |
| Spring Cloud Config      | 2024.0.1  |
| Eureka Discovery Client  | ✅        |

---

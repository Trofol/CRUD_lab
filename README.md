# Spring Cloud Microservices CRUD Lab

Микросервисное приложение для управления пользователями с использованием Spring Cloud паттернов и автоматическими уведомлениями.

## 🏗️ Архитектура

- **Gateway Service** (порт 8080) - API Gateway с маршрутизацией и Circuit Breaker
- **User Service** (порт 8081) - Основной сервис для CRUD операций с пользователями
- **Notification Service** (порт 8082) - Сервис уведомлений (email)
- **Eureka Server** (порт 8761) - Service Discovery
- **Config Server** (порт 8888) - Централизованная конфигурация

## 🚀 Быстрый старт

### 1. Сборка проекта
```bash
build-all.bat
```

### 2. Запуск через Docker Compose
```bash
docker-compose up -d
```

### 3. Проверка работы
- **API Gateway**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Eureka Dashboard**: http://localhost:8761
- **Config Server**: http://localhost:8888

## 📁 Структура проекта

```
CRUD_lab/
├── user-service/          # Основной сервис пользователей
├── notification-service/  # Сервис уведомлений
├── gateway-service/       # API Gateway
├── eureka-server/         # Service Discovery
├── config-server/         # Configuration Server
├── docker-compose.yaml    # Docker Compose конфигурация
├── build-all.bat          # Скрипт сборки
└── README.md              # Этот файл
```

## 🔧 Разработка

### Запуск отдельных сервисов

1. **Eureka Server**:
```bash
cd eureka-server
mvn spring-boot:run
```

2. **Config Server**:
```bash
cd config-server
mvn spring-boot:run
```

3. **User Service**:
```bash
cd user-service
mvn spring-boot:run
```

4. **Notification Service**:
```bash
cd notification-service
mvn spring-boot:run
```

5. **Gateway Service**:
```bash
cd gateway-service
mvn spring-boot:run
```

## 📚 API Документация

После запуска приложения доступна Swagger UI:
- Через Gateway: http://localhost:8080/swagger-ui.html
- Прямо к User Service: http://localhost:8081/swagger-ui.html
- Прямо к Notification Service: http://localhost:8082/swagger-ui.html

## 🔍 Мониторинг

- **Health Checks**: http://localhost:8080/actuator/health
- **Circuit Breaker Metrics**: http://localhost:8080/actuator/circuitbreakers
- **Eureka Dashboard**: http://localhost:8761

## 🛠️ Технологии

- **Spring Boot 3.1.2**
- **Spring Cloud 2022.0.4**
- **Spring Cloud Gateway** - API Gateway
- **Netflix Eureka** - Service Discovery
- **Spring Cloud Config** - External Configuration
- **Resilience4j** - Circuit Breaker
- **PostgreSQL** - База данных
- **Apache Kafka** - Event Streaming
- **Spring Mail** - Email уведомления
- **Docker** - Контейнеризация

## 🏛️ Архитектурная диаграмма

```
┌─────────────────────────────────────────────────────────────────┐
│                        Client (Browser)                        │
└─────────────────────┬───────────────────────────────────────────┘
                      │ HTTP Requests
                      ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Spring Cloud Gateway                        │
│                      (Port 8080)                               │
│  • API Routing (Users + Notifications)                        │
│  • CORS Configuration                                          │
│  • Circuit Breaker                                             │
│  • Load Balancing                                              │
└─────────────────────┬───────────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Service Discovery                           │
│                   (Eureka Server)                              │
│                      (Port 8761)                               │
│  • Service Registration                                        │
│  • Health Monitoring                                           │
│  • Service Discovery                                           │
└─────────────────────┬───────────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────────┐
│                  Configuration Server                          │
│                      (Port 8888)                               │
│  • Centralized Configuration                                   │
│  • Environment-specific Settings                               │
│  • Dynamic Configuration Updates                               │
└─────────────────────┬───────────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────────┐
│                    User Service                                │
│                      (Port 8081)                               │
│  • CRUD Operations                                             │
│  • Business Logic                                              │
│  • Circuit Breaker (Kafka)                                     │
│  • Database Operations                                         │
└─────────────────────┬───────────────────────────────────────────┘
                      │ Kafka Events
                      ▼
┌─────────────────────────────────────────────────────────────────┐
│                Notification Service                            │
│                      (Port 8082)                               │
│  • Email Notifications                                         │
│  • Kafka Event Processing                                      │
│  • REST API for Notifications                                  │
└─────────────────────┬───────────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────────┐
│                    External Services                           │
│  • PostgreSQL Database (Port 5432)                            │
│  • Apache Kafka (Port 9092)                                   │
│  • Zookeeper (Port 2181)                                      │
│  • SMTP Server (Gmail)                                         │
└─────────────────────────────────────────────────────────────────┘
```

## 🔄 Поток данных

1. **Пользователь создается/обновляется** → User Service
2. **User Service отправляет событие** → Kafka
3. **Notification Service получает событие** → Kafka
4. **Notification Service отправляет email** → Gmail SMTP

## 📋 API Endpoints

### Через Gateway (рекомендуется)
- **Users API**: `http://localhost:8080/api/users`
- **Notifications API**: `http://localhost:8080/api/notifications`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`

### Прямой доступ
- **User Service**: `http://localhost:8081/api/users`
- **Notification Service**: `http://localhost:8082/api/notifications`

## ⚙️ Конфигурация

### Email настройки
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
```

### Kafka настройки
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: notification-group
```

## 🚀 Spring Cloud Паттерны

### 1. API Gateway Pattern
- **Реализация**: Spring Cloud Gateway
- **Преимущества**:
  - Единая точка входа
  - Централизованная аутентификация/авторизация
  - Rate limiting
  - Мониторинг трафика

### 2. Service Discovery Pattern
- **Реализация**: Netflix Eureka
- **Преимущества**:
  - Автоматическое обнаружение сервисов
  - Отказоустойчивость
  - Масштабируемость

### 3. Circuit Breaker Pattern
- **Реализация**: Resilience4j
- **Преимущества**:
  - Защита от каскадных сбоев
  - Быстрый отказ при недоступности сервиса
  - Автоматическое восстановление

### 4. External Configuration Pattern
- **Реализация**: Spring Cloud Config
- **Преимущества**:
  - Централизованная конфигурация
  - Версионирование конфигураций
  - Безопасность конфигураций

## 🔧 Troubleshooting

### Проблемы с запуском
1. Убедитесь, что порты 8080-8082, 8761, 8888, 5432, 9092 свободны
2. Проверьте, что Docker запущен
3. Проверьте логи: `docker-compose logs [service-name]`

### Проблемы с email
1. Проверьте настройки SMTP в конфигурации
2. Убедитесь, что используется App Password для Gmail
3. Проверьте логи notification-service

### Проблемы с Kafka
1. Убедитесь, что Zookeeper и Kafka запущены
2. Проверьте настройки bootstrap-servers
3. Проверьте логи user-service и notification-service
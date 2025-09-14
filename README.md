# Spring Cloud Microservices CRUD Lab

Микросервисное приложение для управления пользователями с автоматическими уведомлениями.

## 🏗️ Схема проекта

```mermaid
graph TB
    subgraph "CRUD_lab"
        POM[pom.xml]
        DOCKER[docker-compose.yaml]
        BUILD[build-and-run.bat]
        TEST[test-services.bat]
        
        subgraph "Микросервисы"
            USER[user-service]
            NOTIFY[notification-service]
            GATEWAY[gateway-service]
        end
        
        subgraph "Инфраструктура"
            EUREKA[eureka-server]
            CONFIG[config-server]
            INIT[init-scripts]
        end
    end
    
    POM --> USER
    POM --> NOTIFY
    POM --> GATEWAY
    POM --> EUREKA
    POM --> CONFIG
    
    DOCKER --> USER
    DOCKER --> NOTIFY
    DOCKER --> GATEWAY
    DOCKER --> EUREKA
    DOCKER --> CONFIG
    
    BUILD --> POM
    TEST --> DOCKER
    
    CONFIG --> USER
    CONFIG --> NOTIFY
    CONFIG --> GATEWAY
    
    EUREKA --> USER
    EUREKA --> NOTIFY
    EUREKA --> GATEWAY
    
    INIT --> USER
```

## 📁 Структура проекта

```
CRUD_lab/
├── 📄 README.md                    # Документация проекта
├── 🐳 docker-compose.yaml          # Docker конфигурация
├── 🔧 build-and-run.bat            # Скрипт сборки и запуска
├── 🧪 test-services.bat            # Скрипт тестирования
├── 📦 pom.xml                      # Maven конфигурация
│
├── 👤 user-service/                # Сервис пользователей
├── 📧 notification-service/        # Сервис уведомлений
├── 🌐 gateway-service/             # API Gateway
├── 🔍 eureka-server/               # Service Discovery
├── ⚙️ config-server/               # Configuration Server
└── 🗄️ init-scripts/               # SQL инициализация
```

## 🏗️ Архитектура системы

**Сервисы:**
- **Gateway** (8080) - API Gateway
- **User Service** (8081) - CRUD пользователей
- **Notification Service** (8082) - Email уведомления
- **Eureka Server** (8761) - Service Discovery
- **Config Server** (8888) - Конфигурация
- **PostgreSQL** (5432) - База данных
- **Kafka** (9092) - Message Broker

## 🚀 Быстрый старт

### Запуск системы
```bash
# Сборка и запуск всех сервисов
build-and-run.bat

# Проверка работоспособности
test-services.bat
```

### Ручной запуск
```bash
# Сборка
mvn clean package -DskipTests

# Запуск
docker-compose up -d

# Остановка
docker-compose down
```

## 🔗 Доступные ссылки

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8080
- **User Service**: http://localhost:8081
- **Notification Service**: http://localhost:8082

## 📋 API Endpoints

### User Service
- `GET /api/users` - получить всех пользователей
- `POST /api/users` - создать пользователя
- `GET /api/users/{id}` - получить пользователя по ID
- `PUT /api/users/{id}` - обновить пользователя
- `DELETE /api/users/{id}` - удалить пользователя

### Notification Service
- `GET /api/notifications` - получить все уведомления
- `POST /api/notifications` - отправить уведомление

## 🔄 Поток данных

1. Пользователь создается/обновляется → User Service
2. User Service отправляет событие → Kafka
3. Notification Service получает событие → Kafka
4. Notification Service отправляет email → Gmail SMTP

## 🛠️ Технологии

- **Spring Boot 3.1.2**
- **Spring Cloud 2022.0.4**
- **PostgreSQL** - База данных
- **Apache Kafka** - Event Streaming
- **Docker** - Контейнеризация

## 📊 Мониторинг

```bash
# Просмотр логов
docker-compose logs -f

# Статус контейнеров
docker-compose ps

# Использование ресурсов
docker stats
```

## 🐛 Устранение неполадок

### Проблемы с запуском
1. Убедитесь, что Docker запущен
2. Проверьте, что порты 8080-8082, 8761, 8888, 5432, 9092 свободны
3. Проверьте логи: `docker-compose logs [service-name]`

### Очистка
```bash
# Остановка и удаление контейнеров
docker-compose down

# Удаление образов
docker-compose down --rmi all

# Удаление volumes
docker-compose down -v
```

---

**Система готова к использованию!** 🎉

Для запуска: `build-and-run.bat`
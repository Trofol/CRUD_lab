# Spring Cloud Microservices CRUD Lab

Микросервисное приложение для управления пользователями с автоматическими уведомлениями.

## 🚀 Быстрый старт

```bash
# Запуск всей системы
build-and-run.bat

# Проверка работоспособности
test-services.bat
```

## 📋 Что включено

- **User Service** - CRUD операции с пользователями
- **Notification Service** - Email уведомления
- **API Gateway** - Единая точка входа
- **Service Discovery** - Eureka Server
- **Configuration Server** - Централизованная конфигурация
- **PostgreSQL** - База данных
- **Kafka** - Message Broker для событий

## 🔗 Доступные ссылки

После запуска системы:

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

## 🔄 Как это работает

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

## 📊 Управление системой

```bash
# Просмотр логов
docker-compose logs -f

# Статус контейнеров
docker-compose ps

# Остановка системы
docker-compose down

# Перезапуск
docker-compose restart
```

## 🐛 Устранение неполадок

### Проблемы с запуском
1. Убедитесь, что Docker запущен
2. Проверьте, что порты 8080-8082, 8761, 8888, 5432, 9092 свободны
3. Проверьте логи: `docker-compose logs [service-name]`

### Очистка
```bash
# Полная очистка
docker-compose down -v --rmi all
```

---

**Система готова к использованию!** 🎉

Для запуска: `build-and-run.bat`
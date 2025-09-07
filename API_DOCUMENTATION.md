RESTful API для управления пользователями с поддержкой HATEOAS и Swagger документацией.

## Документация

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

API поддерживает HATEOAS - каждый ответ содержит ссылки для навигации.

## Endpoints

| Метод | Путь | Описание |
|-------|------|----------|
| GET | `/api/users` | Получить всех пользователей |
| GET | `/api/users/{id}` | Получить пользователя по ID |
| POST | `/api/users` | Создать пользователя |
| PUT | `/api/users/{id}` | Обновить пользователя |
| DELETE | `/api/users/{id}` | Удалить пользователя |

## Технологии

- Spring Boot 3.1.2
- Spring HATEOAS
- Spring Data JPA
- PostgreSQL + Kafka
- Swagger/OpenAPI 3

## Особенности

- **HATEOAS** - ссылки для навигации по API
- **Swagger** - интерактивная документация
- **Валидация** - проверка входных данных
- **Kafka** - асинхронные события

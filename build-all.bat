@echo off
echo Сборка всех Spring Cloud модулей...

echo.
echo Сборка родительского проекта...
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo Ошибка сборки родительского проекта
    exit /b 1
)

echo.
echo Все модули успешно собраны!
echo Теперь можно запустить: docker-compose up -d

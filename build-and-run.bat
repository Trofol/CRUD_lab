@echo off
echo Building and Running Microservices System...

echo.
echo Building project...
call mvn clean package -DskipTests
if %ERRORLEVEL% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo.
echo Starting services...
docker-compose up -d

echo.
echo System is ready!
echo - Swagger UI: http://localhost:8080/swagger-ui.html
echo - Eureka Dashboard: http://localhost:8761
echo.
echo Commands:
echo - View logs: docker-compose logs -f
echo - Stop: docker-compose down
echo - Test: test-services.bat
echo.
pause

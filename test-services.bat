@echo off
echo Testing Microservices System...

echo.
echo Testing services...
curl -s http://localhost:8761/actuator/health && echo Eureka: OK || echo Eureka: FAILED
curl -s http://localhost:8888/actuator/health && echo Config: OK || echo Config: FAILED
curl -s http://localhost:8081/actuator/health && echo User: OK || echo User: FAILED
curl -s http://localhost:8082/actuator/health && echo Notification: OK || echo Notification: FAILED
curl -s http://localhost:8080/actuator/health && echo Gateway: OK || echo Gateway: FAILED

echo.
echo Testing API...
curl -s http://localhost:8080/api/users && echo API: OK || echo API: FAILED

echo.
echo Testing completed!
pause

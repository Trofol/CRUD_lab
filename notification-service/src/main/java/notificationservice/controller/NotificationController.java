package notificationservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import notificationservice.dto.OperationType;
import notificationservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final UserService userService;

    public NotificationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@Valid @RequestBody NotificationRequest request) {
        userService.sendEmail(request.getEmail(), request.getOperation());
        return ResponseEntity.ok("Email отправлен");
    }

    @Data
    public static class NotificationRequest {
        @Email(message = "Неверный email")
        @NotNull(message = "Email обязателен")
        private String email;
        
        @NotNull(message = "Операция обязательна")
        private OperationType operation;
    }
}

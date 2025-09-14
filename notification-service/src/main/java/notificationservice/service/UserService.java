package notificationservice.service;

import notificationservice.dto.OperationType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final JavaMailSender mailSender;

    public UserService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String email, OperationType operation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Уведомление от сервиса");
        message.setText(operation.getEmailText());
        mailSender.send(message);
    }
}

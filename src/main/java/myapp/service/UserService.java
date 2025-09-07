package myapp.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myapp.dto.OperationType;
import myapp.dto.UserEvent;
import myapp.dto.UserDto;
import myapp.exceptions.UserNotFoundException;
import myapp.exceptions.ValidationException;
import myapp.mapper.UserMapper;
import myapp.model.User;
import myapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Value("${app.kafka.topic-name:user-events}")
    private String topicName;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        return UserMapper.toDto(user);
    }

    public UserDto createUser(@Valid UserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ValidationException("Email уже занят");
        }

        User user = UserMapper.toEntity(dto);
        user.setId(null);
        User saved = userRepository.save(user);
        log.info("Успешно создан пользователь: {}", saved.getEmail());

        sendUserEvent(OperationType.CREATE, saved.getEmail());

        return UserMapper.toDto(saved);
    }

    public UserDto updateUser(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            if (!dto.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
                throw new ValidationException("Email уже занят");
            }
            user.setEmail(dto.getEmail());
        }
        if (dto.getAge() != null) {
            user.setAge(dto.getAge());
        }

        User updated = userRepository.save(user);
        log.info("Пользователь с ID {} обновлён", id);
        
        sendUserEvent(OperationType.UPDATE, updated.getEmail());
        
        return UserMapper.toDto(updated);
    }

    public void deleteUser(Long id) {
        UserDto user = getUserById(id);
        userRepository.deleteById(id);
        log.info("Пользователь с ID {} удалён", id);

        sendUserEvent(OperationType.DELETE, user.getEmail());
    }

    private void sendUserEvent(OperationType operation, String email) {
        try {
            UserEvent event = new UserEvent(operation, email);
            kafkaTemplate.send(topicName, event)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Событие '{}' отправлено для email: {}", operation.name(), email);
                        } else {
                            log.error("Ошибка отправки события '{}' для email: {}", operation.name(), email, ex);
                        }
                    });
        } catch (Exception e) {
            log.error("Ошибка отправки события для email: {}", email, e);
        }
    }
}

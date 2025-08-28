package myapp.service;

import jakarta.transaction.Transactional;
import myapp.dto.UserDto;
import jakarta.validation.Valid;
import myapp.mapper.UserMapper;
import myapp.model.User;
import myapp.repository.UserRepository;
import myapp.exceptions.UserNotFoundException;
import myapp.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    private final UserRepository userRepository;

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
        if (dto.getEmail() == null || !EMAIL_PATTERN.matcher(dto.getEmail()).matches()) {
            log.warn("Некорректный email: {}", dto.getEmail());
            throw new ValidationException("Некорректный email");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserNotFoundException("Email уже занят");
        }

        User user = UserMapper.toEntity(dto);
        user.setId(null);
        User saved = userRepository.save(user);
        log.info("Успешно создан пользователь: {}", saved.getEmail());
        return UserMapper.toDto(saved);
    }

    public UserDto updateUser(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            if (!EMAIL_PATTERN.matcher(dto.getEmail()).matches()) {
                throw new ValidationException("Некорректный формат email");
            }
            if (!dto.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
                throw new UserNotFoundException("Email уже занят");
            }
            user.setEmail(dto.getEmail());
        }
        if (dto.getAge() != null) {
            if (dto.getAge() < 1 || dto.getAge() > 120) {
                throw new ValidationException("Возраст должен быть от 1 до 120");
            }
            user.setAge(dto.getAge());
        }

        User updated = userRepository.save(user);
        log.info("Пользователь с ID {} обновлён", id);
        return UserMapper.toDto(updated);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        log.info("Пользователь с ID {} удалён", id);
    }
}

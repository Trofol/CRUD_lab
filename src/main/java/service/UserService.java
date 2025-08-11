package service;

import dao.UserDao;
import exceptions.DaoException;
import exceptions.ValidationException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private static final String EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUser(User user) throws DaoException {

        if (user.getEmail() == null || !user.getEmail().matches(EMAIL_REGEX)) {
            log.warn("Некорректный email: {}", user.getEmail());
            throw new ValidationException("Некорректный email");
        }
        try {
            userDao.create(user);
            log.info("Успешно создан пользователь: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Ошибка при создании пользователя: {}", e.getMessage());
            if (e.getMessage().contains("unique constraint") || e.getMessage().contains("duplicate key")) {
                throw new DaoException("Email уже занят");
            }
            throw new DaoException("Ошибка создания пользователя", e);
        }
    }

    public User getUserById(Long id) throws DaoException {
        return userDao.findById(id)
                .orElseThrow(() -> new DaoException("Пользователь не найден"));
    }

    public List<User> getAllUsers() throws DaoException {
        return userDao.findAll();
    }

    public User updateUser(Long id, String newName, String newEmail, Integer newAge)
            throws DaoException, ValidationException {

        User user = userDao.findById(id).orElseThrow(() -> new DaoException("Пользователь с ID " + id + " не найден"));

        if (newName != null) {
            user.setName(newName);
        }

        if (newEmail != null) {
            if (!newEmail.matches(EMAIL_REGEX)) {
                throw new ValidationException("Некорректный формат email");
            }
            user.setEmail(newEmail);
        }

        if (newAge != null) {
            if (newAge < 1 || newAge > 120) {
                throw new ValidationException("Возраст должен быть от 1 до 120");
            }
            user.setAge(newAge);
        }

        userDao.update(user);
        log.info("Пользователь с ID {} обновлён", id);
        return user;
    }

    public void deleteUser(Long id) throws DaoException {
        userDao.delete(id);
        log.info("Пользователь с ID {} удалён", id);
    }
}

package service;

import exceptions.ValidationException;

public class ValidationService {
    private static final int MIN_AGE = 1;
    private static final int MAX_AGE = 100;
    private static final int MAX_NAME_LENGTH = 100;
    private static final String EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    public void validateName(String name) throws ValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Имя не может быть пустым");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new ValidationException("Имя слишком длинное (макс. " + MAX_NAME_LENGTH + " символов)");
        }
    }

    public void validateEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email не может быть пустым");
        }
        if (!email.matches(EMAIL_REGEX)) {
            throw new ValidationException("Некорректный формат email");
        }
    }

    public void validateAge(Integer age) throws ValidationException {
        if (age == null) {
            throw new ValidationException("Возраст не может быть пустым");
        }
        if (age < MIN_AGE || age > MAX_AGE) {
            throw new ValidationException("Возраст должен быть от " + MIN_AGE + " до " + MAX_AGE);
        }
    }
}

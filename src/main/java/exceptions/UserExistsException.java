package exceptions;

public class UserExistsException extends DaoException {
    public UserExistsException(String email) {
        super("Пользователь с email " + email + " уже существует");
    }
}

package exceptions;

public class UserNotFoundException extends DaoException {
    public UserNotFoundException(Long id) {
        super("Пользователь с ID " + id + " не найден");
    }
}

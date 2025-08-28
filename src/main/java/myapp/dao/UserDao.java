package myapp.dao;

import myapp.exceptions.UserNotFoundException;
import myapp.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    void create(User user) throws UserNotFoundException;
    Optional<User> findById(Long id) throws UserNotFoundException;
    List<User> findAll() throws UserNotFoundException;
    void update(User user) throws UserNotFoundException;
    void delete(Long id) throws UserNotFoundException;
}
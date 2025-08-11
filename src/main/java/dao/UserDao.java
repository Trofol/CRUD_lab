package dao;

import exceptions.DaoException;
import model.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    void create(User user) throws DaoException;
    Optional<User> findById(Long id) throws DaoException;
    List<User> findAll() throws DaoException;
    void update(User user) throws DaoException;
    void delete(Long id) throws DaoException;
}
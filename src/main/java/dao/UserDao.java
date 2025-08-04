package dao;

import exceptions.DaoException;
import model.User;
import java.util.List;

public interface UserDao {
    void create(User user) throws DaoException;
    User findById(Long id) throws DaoException;
    List <User> findAll() throws DaoException;
    void update(User user) throws DaoException;
    void delete(Long id) throws DaoException;
}

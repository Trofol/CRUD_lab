package dao;

import model.User;
import java.util.List;
public interface UserDao {
    void create(User user);
    User findById(Long id);
    List <User> findAll();
    void update(User user);
    void delete(Long id);
}

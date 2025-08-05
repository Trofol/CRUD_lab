package dao;

import exceptions.DaoException;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;

//логи
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class UserDaoImpl implements UserDao {
    private final SessionFactory sessionFactory;
    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }



    @Override
    public void create(User user) throws DaoException {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
            log.info("Создан пользователь: ID={}, Email={}", user.getId(), user.getEmail());
        } catch (Exception e) {
            log.error("Ошибка создания пользователя: {}", e.getMessage());
            if (tx != null) tx.rollback();
            throw new DaoException("Ошибка создания пользователя", e);
        }
    }

    @Override
    public User findById(Long id) throws DaoException {
        try (Session session = sessionFactory.openSession()) {
            log.info("Поиск пользователя по ID: {}", id);
            return session.get(User.class, id);
        } catch (Exception e) {
            log.error("Ошибка поиска пользователя: {}", e.getMessage());
            throw new DaoException("Ошибка поиска пользователя", e);
        }
    }

    @Override
    public List<User> findAll() throws DaoException {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            log.error("Ошибка загрузки пользователя: {}", e.getMessage());
            throw new DaoException("Ошибка загрузки пользователей", e);
        }
    }

    @Override
    public void update(User user) throws DaoException {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.update(user);
            tx.commit();
            log.info("Обновление пользователя : {}", user);
        } catch (Exception e) {
            log.error("Ошибка обновления пользователя: {}", e.getMessage());
            if (tx != null) tx.rollback();
            throw new DaoException("Ошибка обновления пользователя", e);
        }
    }

    @Override
    public void delete(Long id) throws DaoException {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) session.delete(user);
            tx.commit();
            log.info("Удаление пользователя : {}", user);
        } catch (Exception e) {
            log.error("Ошибка удаления пользователя: {}", e.getMessage());
            if (tx != null) tx.rollback();
            throw new DaoException("Ошибка удаления пользователя", e);
        }
    }
}

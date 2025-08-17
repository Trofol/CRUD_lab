package myapp.dao;

import myapp.exceptions.DaoException;
import myapp.model.User;
import org.hibernate.SessionFactory;
import myapp.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UserDaoImpl implements UserDao {
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public UserDaoImpl() {

    }

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(User user) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            log.debug("Создан пользователь с ID: {}", user.getId());
        } catch (Exception e) {
            log.error("Ошибка создания пользователя: ", e);
            if (tx != null && tx.isActive()) tx.rollback();
            throw new DaoException("Ошибка создания: ", e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user != null) {
                log.debug("Найден пользователь по ID: {}", id);
            } else {
                log.debug("Пользователь с ID  {} не найден", id);
            }
            return Optional.ofNullable(user);
        } catch (Exception e) {
            log.error("Ошибка поиска пользователя по ID  {}: {}", id, e.getMessage());
            throw new DaoException("Ошибка поиска", e);
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("FROM User", User.class);
            List<User> users = query.getResultList();
            log.debug("Найдены {} пользователя", users.size());
            return users;
        } catch (Exception e) {
            log.error("Ошибка поиска всех пользователей: {}", e.getMessage());
            throw new DaoException("Ошибка поиска всех пользователей", e);
        }
    }

    @Override
    public void update(User user) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
            log.debug("Изменен пользователь с ID : {}", user.getId());
        } catch (Exception e) {
            log.error("Ошибка изменения пользователя: {}", e.getMessage());
            if (tx != null && tx.isActive()) tx.rollback();
            throw new DaoException("Ошибка изменения пользователя", e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                log.debug("Удален пользователь с ID: {}", id);
            } else {
                log.warn("Удаляемый пользователь не найден: {}", id);
            }
            tx.commit();
        } catch (Exception e) {
            log.error("Ошибка удаления пользователя с ID {}: {}", id, e.getMessage());
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new DaoException("Ошибка удаления пользователя", e);
        }
    }


}
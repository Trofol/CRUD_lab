package dao;

import model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDaoImplIT {

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("test-db")
                    .withUsername("test")
                    .withPassword("test");

    private SessionFactory sessionFactory;
    private UserDao userDao;

    @BeforeAll
    void setUp() {
        // Конфиг Hibernate под контейнер Postgres
        Configuration cfg = new Configuration();
        cfg.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        cfg.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        cfg.setProperty("hibernate.connection.username", postgres.getUsername());
        cfg.setProperty("hibernate.connection.password", postgres.getPassword());
        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        cfg.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        cfg.setProperty("hibernate.show_sql", "false");
        cfg.addAnnotatedClass(User.class);

        sessionFactory = cfg.buildSessionFactory();
        userDao = new UserDaoImpl(sessionFactory);
    }

    @BeforeEach
    void cleanDatabase() {
        try (var session = sessionFactory.openSession()) {
            var tx = session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE users RESTART IDENTITY CASCADE").executeUpdate();
            tx.commit();
        }
    }

    @AfterAll
    void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    void createAndFindById() {
        User user = User.builder()
                .name("Test")
                .email("test@example.com")
                .age(12)
                .build();

        userDao.create(user);

        assertNotNull(user.getId());
        Optional<User> found = userDao.findById(user.getId());
        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getName());
    }

    @Test
    void findAllInitiallyEmpty() {
        List<User> allUsers = userDao.findAll();
        assertNotNull(allUsers);
        assertTrue(allUsers.isEmpty());
    }

    @Test
    void updateUser() {
        User user = User.builder()
                .name("DopTest")
                .email("doptest@example.com")
                .age(25)
                .build();
        userDao.create(user);

        user.setName("DopTest updated");
        user.setAge(26);
        userDao.update(user);

        Optional<User> updated = userDao.findById(user.getId());
        assertTrue(updated.isPresent());
        assertEquals("DopTest updated", updated.get().getName());
        assertEquals(26, updated.get().getAge());
    }

    @Test
    void deleteUser() {
        User user = User.builder()
                .name("ToDelete")
                .email("delete@example.com")
                .age(40)
                .build();
        userDao.create(user);
        Long id = user.getId();

        userDao.delete(id);
        Optional<User> afterDelete = userDao.findById(id);
        assertFalse(afterDelete.isPresent());
    }
}

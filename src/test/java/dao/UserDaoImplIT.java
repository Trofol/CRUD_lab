package dao;

import myapp.dao.UserDao;
import myapp.dao.UserDaoImpl;
import myapp.model.User;
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

    // Константы
    private static final String TEST_NAME = "TestUser";
    private static final String TEST_EMAIL = "test@example.com";
    private static final int TEST_AGE = 20;

    private static final String DOP_NAME = "DopTest";
    private static final String DOP_EMAIL = "dop@example.com";
    private static final int DOP_AGE = 25;

    private static final String DELETE_NAME = "ToDelete";
    private static final String DELETE_EMAIL = "delete@example.com";
    private static final int DELETE_AGE = 40;

    @Container
    private static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("test-db")
                    .withUsername("test")
                    .withPassword("test");

    private SessionFactory sessionFactory;
    private UserDao userDao;

    @BeforeAll
    void setUp() {
        Configuration cfg = new Configuration();
        cfg.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        cfg.setProperty("hibernate.connection.url", POSTGRES.getJdbcUrl());
        cfg.setProperty("hibernate.connection.username", POSTGRES.getUsername());
        cfg.setProperty("hibernate.connection.password", POSTGRES.getPassword());
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
        Long userId;
        try (var session = sessionFactory.openSession()) {
            var tx = session.beginTransaction();
            User user = User.builder()
                    .name(TEST_NAME)
                    .email(TEST_EMAIL)
                    .age(TEST_AGE)
                    .build();
            session.persist(user);
            tx.commit();
            userId = user.getId();
        }

        Optional<User> found = userDao.findById(userId);
        assertTrue(found.isPresent());
        assertEquals(TEST_NAME, found.get().getName());
        assertEquals(TEST_EMAIL, found.get().getEmail());
    }

    @Test
    void findAllInitiallyEmpty() {
        List<User> allUsers = userDao.findAll();
        assertNotNull(allUsers);
        assertTrue(allUsers.isEmpty());
    }

    @Test
    void updateUser() {
        Long userId;
        try (var session = sessionFactory.openSession()) {
            var tx = session.beginTransaction();
            User user = User.builder()
                    .name(DOP_NAME)
                    .email(DOP_EMAIL)
                    .age(DOP_AGE)
                    .build();
            session.persist(user);
            tx.commit();
            userId = user.getId();
        }

        User toUpdate = userDao.findById(userId).orElseThrow();
        toUpdate.setName("UpdatedName");
        toUpdate.setAge(30);
        userDao.update(toUpdate);

        Optional<User> updated = userDao.findById(userId);
        assertTrue(updated.isPresent());
        assertEquals("UpdatedName", updated.get().getName());
        assertEquals(30, updated.get().getAge());
    }

    @Test
    void deleteUser() {
        Long userId;

        try (var session = sessionFactory.openSession()) {
            var tx = session.beginTransaction();
            User user = User.builder()
                    .name(DELETE_NAME)
                    .email(DELETE_EMAIL)
                    .age(DELETE_AGE)
                    .build();
            session.persist(user);
            tx.commit();
            userId = user.getId();
        }

        userDao.delete(userId);
        Optional<User> afterDelete = userDao.findById(userId);
        assertFalse(afterDelete.isPresent());
    }

    @Test
    void existsByEmail() {
        try (var session = sessionFactory.openSession()) {
            var tx = session.beginTransaction();
            User user = User.builder()
                    .name("Checker")
                    .email("checker@example.com")
                    .age(30)
                    .build();
            session.persist(user);
            tx.commit();
        }
    }
}

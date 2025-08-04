import dao.UserDao;
import dao.UserDaoImpl;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AppRunner;
import service.UserService;


public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        log.info("Инициализация приложения...");

        SessionFactory sessionFactory = null;
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
            log.info("Hibernate инициализирован");

            UserDao userDao = new UserDaoImpl(sessionFactory);
            UserService userService = new UserService(userDao);
            AppRunner appRunner = new AppRunner(userService);

            log.info("Запуск пользовательского интерфейса");
            appRunner.start();

        } catch (Exception e) {
            log.error("Критическая ошибка при работе приложения", e);
            System.err.println("Произошла критическая ошибка: " + e.getMessage());
        } finally {
            if (sessionFactory != null) {
                try {
                    sessionFactory.close();
                    log.info("SessionFactory закрыт");
                } catch (Exception e) {
                    log.error("Ошибка при закрытии SessionFactory", e);
                }
            }
            log.info("Приложение завершило работу");
        }
    }

}
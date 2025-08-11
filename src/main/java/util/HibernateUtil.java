package util;

import dao.UserDaoImpl;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public final class HibernateUtil {
    private static SessionFactory sessionFactory;
    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
    private HibernateUtil() {}

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {

            try {
                sessionFactory = new Configuration()
                        .configure("hibernate.cfg.xml")
                        .buildSessionFactory();
                log.info("SessionFactory успешно создан");
            } catch (Exception e) {
                log.error("Ошибка инициализации SessionFactory", e);
                throw new RuntimeException("Не удалось инициализировать SessionFactory", e);
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            log.info("SessionFactory закрыт");
        }
    }
}
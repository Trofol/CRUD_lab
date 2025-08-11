import dao.UserDao;
import dao.UserDaoImpl;
import lombok.extern.slf4j.Slf4j;
import service.AppRunner;
import service.UserService;
import util.HibernateUtil;
@Slf4j
public class Main {

    public static void main(String[] args) {
        log.info("Инициализация приложения...");

        try {
            // Инициализация DAO и сервисного слоя
            UserDao userDao = new UserDaoImpl();
            UserService userService = new UserService(userDao);
            AppRunner appRunner = new AppRunner(userService);

            log.info("Запуск пользовательского интерфейса");
            appRunner.start();

        } catch (Exception e) {
            log.error("Критическая ошибка при работе приложения", e);
            System.err.println("Произошла критическая ошибка: " + e.getMessage());
        } finally {
            HibernateUtil.shutdown(); // Закрываем SessionFactory через утилиту
            log.info("Приложение завершило работу");
        }
    }
}
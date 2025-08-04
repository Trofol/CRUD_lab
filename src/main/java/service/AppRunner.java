package service;


import exceptions.ValidationException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class AppRunner {
    private final UserService userService;
    private final Scanner scanner;
    private static final Logger log = LoggerFactory.getLogger(AppRunner.class);

    public AppRunner(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        log.info("Запуск приложения");
        try {
            while (true) {
                printMenu();
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> createUser();
                    case 2 -> showUser();
                    case 3 -> showAllUsers();
                    case 4 -> updateUser();
                    case 5 -> deleteUser();
                    case 0 -> { return; }
                    default -> System.out.println("Неверный ввод");
                }
            }
        } finally {
            scanner.close();
            log.info("Приложение завершено");
        }
    }

    private void createUser() {
        try {
            log.debug("Начато создание пользователя");
            User user = new User();
            System.out.print("Введите email: ");
            user.setEmail(scanner.nextLine());

            System.out.print("Введите имя: ");
            user.setName(scanner.nextLine());

            System.out.print("Введите возраст: ");
            user.setAge(scanner.nextInt());

            userService.createUser(user);
            System.out.println("Пользователь создан!");
        } catch (Exception e) {
            log.warn("Ошибка создания: {}", e.getMessage());
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void showUser() {
        try {
            System.out.print("Введите ID пользователя: ");
            Long id = Long.parseLong(scanner.nextLine());
            User user = userService.getUserById(id);
            printUserDetails(user);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void showAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            if (users.isEmpty()) {
                System.out.println("Список пользователей пуст");
                return;
            }

            System.out.println("\n=== Список пользователей ===");
            System.out.printf("%-5s %-20s %-25s %-5s%n", "ID", "Имя", "Email", "Возраст");
            System.out.println("-".repeat(60));

            for (User user : users) {
                System.out.printf("%-5d %-20s %-25s %-5d%n",
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getAge());
            }
        } catch (Exception e) {
            System.out.println("Ошибка при загрузке: " + e.getMessage());
        }
    }

    private void updateUser() {
        try {
            System.out.print("Введите ID пользователя: ");
            Long id = Long.parseLong(scanner.nextLine());

            User user = userService.getUserById(id);
            System.out.println("Текущие данные:");
            printUserDetails(user);

            System.out.println("\nВведите новые данные (оставьте пустым чтобы не менять):");

            System.out.print("Имя [" + user.getName() + "]: ");
            String nameInput = scanner.nextLine();
            String newName = nameInput.isEmpty() ? null : nameInput;

            System.out.print("Email [" + user.getEmail() + "]: ");
            String emailInput = scanner.nextLine();
            String newEmail = emailInput.isEmpty() ? null : emailInput;

            System.out.print("Возраст [" + user.getAge() + "]: ");
            String ageInput = scanner.nextLine();
            Integer newAge = ageInput.isEmpty() ? null : Integer.parseInt(ageInput);

            // Теперь типы точно совпадают
            User updatedUser = userService.updateUser(id, newName, newEmail, newAge);

            System.out.println("Данные обновлены:");
            printUserDetails(updatedUser);

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: возраст должен быть числом");
        } catch (ValidationException e) {
            System.out.println("Ошибка валидации: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void deleteUser() {
        try {
            System.out.print("Введите ID пользователя: ");
            Long id = Long.parseLong(scanner.nextLine());

            User user = userService.getUserById(id);
            System.out.println("Найден пользователь:");
            printUserDetails(user);

            System.out.print("Вы уверены, что хотите удалить? (y/n): ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("y")) {
                userService.deleteUser(id);
                System.out.println("Пользователь удален");
            } else {
                System.out.println("Удаление отменено");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void printUserDetails(User user) {
        System.out.println("\n=== Данные пользователя ===");
        System.out.println("ID: " + user.getId());
        System.out.println("Имя: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Возраст: " + user.getAge());
        System.out.println("Дата создания: " +
                (user.getCreatedAt() != null ?
                        user.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) :
                        "не указана"));
    }

    private void printMenu() {
        System.out.println("\n=== Меню ===");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Найти пользователя по ID");
        System.out.println("3. Показать всех пользователей");
        System.out.println("4. Обновить пользователя");
        System.out.println("5. Удалить пользователя");
        System.out.println("0. Выход");
        System.out.print("> ");
    }
}

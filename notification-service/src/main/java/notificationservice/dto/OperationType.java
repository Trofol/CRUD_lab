package notificationservice.dto;

public enum OperationType {
    CREATE("Здравствуйте! Ваш аккаунт на сайте был успешно создан."),
    DELETE("Здравствуйте! Ваш аккаунт был удалён.");

    private final String emailText;

    OperationType(String emailText) {
        this.emailText = emailText;
    }

    public String getEmailText() {
        return emailText;
    }
}

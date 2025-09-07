package myapp.dto;

import lombok.Getter;

@Getter
public enum OperationType {
    CREATE("CREATE", "Пользователь создан"),
    DELETE("DELETE", "Пользователь удалён");

    private final String code;
    private final String description;

    OperationType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static OperationType fromCode(String code) {
        for (OperationType op : values()) {
            if (op.code.equalsIgnoreCase(code)) return op;
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}

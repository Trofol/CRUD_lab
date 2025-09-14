package myapp.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;

    @NotBlank(message = "Имя обязательно")
    private String name;

    @Email(message = "Укажите корректный e-mail")
    @NotBlank(message = "Email обязателен")
    private String email;

    @NotNull(message = "Возраст обязателен")
    @Min(value = 1, message = "Возраст не может быть меньше 1")
    @Max(value = 120, message = "Возраст не может быть больше 120")
    private Integer age;
}
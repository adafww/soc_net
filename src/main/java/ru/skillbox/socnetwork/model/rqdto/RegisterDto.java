package ru.skillbox.socnetwork.model.rqdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
@Schema(description = "Сущность для регистрации")
public class RegisterDto {
    @Schema(example = "some@mail.ru")
    @Email(message = "Email is not correct")
    private String email;
    @JsonProperty("passwd1")
    @Schema(example = "12345678", minLength = 8)
    @Pattern(regexp = ".{8,}", message = "Пароль не может быть менее 8 символов")
    private String firstPassword;
    @JsonProperty("passwd2")
    @Schema(description = "Подтверждение пароля", example = "12345678", minLength = 8)
    @Pattern(regexp = ".{8,}", message = "Пароль не может быть менее 8 символов")
    private String secondPassword;
    @Schema(example = "Иван")
    private String firstName;
    @Schema(example = "Иванов")
    private String lastName;
    @JsonProperty("captcha_id")
    private Long codeId;
    private String code;

    public boolean passwordsEqual() {
        return firstPassword.equals(secondPassword);
    }
}

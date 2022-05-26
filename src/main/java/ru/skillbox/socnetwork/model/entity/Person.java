package ru.skillbox.socnetwork.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import lombok.Data;
import ru.skillbox.socnetwork.model.entity.enums.TypePermission;

@Data
@Schema(description = "Пользователь")
public class Person {
    private Integer id;
    @JsonProperty("first_name")
    @Schema(example = "Иван")
    private String firstName;
    @JsonProperty("last_name")
    @Schema(example = "Иванов")
    private String lastName;
    @JsonProperty("reg_date")
    @Schema(description = "Дата регистрации", example = "1630627200000")
    private Long regDate;
    @JsonProperty("birth_date")
    @Schema(description = "Дата рождения", example = "1630627200000")
    private Long birthDate;
    @Email
    @Schema(example = "some@mail.ru")
    private String email;
    @Schema(example = "71234567890")
    private String phone;
    @Schema(example = "12345678")
    private String password;
    @Schema(example = "https://host.ru/image.jpg")
    private String photo;
    @Schema(example = "Я узнал, что у меня есть огромная семья...")
    private String about;
    @Schema(example = "Дефолт-сити")
    private String city;
    @Schema(example = "Россия")
    private String country;
    private String confirmationCode;
    private Boolean isApproved;
    private TypePermission messagesPermission;
    private Long lastOnlineTime;
    @JsonProperty("is_blocked")
    private Boolean isBlocked;
    @JsonProperty("is_deleted")
    @Schema(description = "Пользователь помечен к удалению")
    private Boolean isDeleted;

    public Person() {
        this.messagesPermission = TypePermission.ALL;
        this.isApproved = true;
        this.isBlocked = false;
    }

}

package ru.skillbox.socnetwork.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.socnetwork.model.entity.enums.TypePermission;

import javax.validation.constraints.Email;

@Data
@Schema(description = "Пользователь")
public class Person {
    private Integer id;
    @Schema(example = "Иван")
    private String firstName;
    @Schema(example = "Иванов")
    private String lastName;
    @Schema(description = "Дата регистрации", example = "1630627200000")
    private Long regDate;
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
    private Boolean isBlocked;
    @Schema(description = "Пользователь помечен к удалению")
    private Boolean isDeleted;

    public Person() {
        this.messagesPermission = TypePermission.ALL;
        this.isApproved = true;
        this.isBlocked = false;
    }

}

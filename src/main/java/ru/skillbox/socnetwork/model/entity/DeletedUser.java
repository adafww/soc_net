package ru.skillbox.socnetwork.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Пользователь, помеченный к удалению")
public class DeletedUser {
  private Integer id;
  @Schema(description = "Идентификатор пользователя", example = "13")
  private Integer personId;
  @Schema(description = "Путь к исходному фото", example = "https://host.ru/image.jpg")
  private String photo;
  @Schema(description = "Имя пользователя до удаления", example = "Иван")
  private String firstName;
  @Schema(description = "Фамилия пользователя до удаления", example = "Иванов")
  private String lastName;
  @Schema(description = "Дата полного удаления", example = "1630627200000")
  private Long expire;

  public DeletedUser(){}
  public DeletedUser(Person person) {
    this.personId = person.getId();
    this.photo = person.getPhoto();
    this.firstName = person.getFirstName();
    this.lastName = person.getLastName();
    expire = System.currentTimeMillis() + 300_000; // Default: 259200000
  }
}
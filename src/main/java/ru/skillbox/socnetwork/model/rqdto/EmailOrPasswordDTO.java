package ru.skillbox.socnetwork.model.rqdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Данные для восстановления")
public class EmailOrPasswordDTO {
  @Schema(description = "Временный токен")
  private String token;
  private String email;
  private String password;
}

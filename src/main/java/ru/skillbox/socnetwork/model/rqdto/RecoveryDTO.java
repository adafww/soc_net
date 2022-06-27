package ru.skillbox.socnetwork.model.rqdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Email для восстановления данных")
public class RecoveryDTO {
  @Schema(example = "some@mail.ru")
  private String email;
}

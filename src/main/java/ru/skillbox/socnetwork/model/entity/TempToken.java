package ru.skillbox.socnetwork.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Генерируемый токен для ссылки по изменению данных")
public class TempToken {
  private Integer id;
  private String email;
  private String token;

  public TempToken(){}
  public TempToken(String email, String token){
    this.email = email;
    this.token = token;
  }
}

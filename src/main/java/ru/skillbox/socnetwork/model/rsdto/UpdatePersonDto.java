
package ru.skillbox.socnetwork.model.rsdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skillbox.socnetwork.model.entity.Person;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema
public class UpdatePersonDto {
  @JsonProperty("first_name")
  private String firstName;
  @JsonProperty("last_name")
  private String lastName;
  @JsonProperty("birth_date")
  private String birthDate;
  private String phone;
  private String photo;
  private String about;
  private String city;
  private String country;
  @JsonProperty("messages_permission")
  private String messagesPermission;

  public UpdatePersonDto(Person person) {
    this.photo = person.getPhoto();
  }
}

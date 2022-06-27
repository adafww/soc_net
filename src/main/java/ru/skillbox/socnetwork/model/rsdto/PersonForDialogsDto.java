package ru.skillbox.socnetwork.model.rsdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(hidden = true)
public class PersonForDialogsDto {
    private Integer id;
    private String photo;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("e_mail")
    private String eMail;
    @JsonProperty("last_online_time")
    private Long lastOnlineTime;
}

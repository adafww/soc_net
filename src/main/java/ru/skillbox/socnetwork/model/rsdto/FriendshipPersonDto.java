package ru.skillbox.socnetwork.model.rsdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(hidden = true)
public class FriendshipPersonDto {

    @JsonProperty("user_id")
    private Integer userId;
    private String status;

}

package ru.skillbox.socnetwork.model.rqdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Список пользователей в диалоге")
public class DialogRequest {
    @JsonProperty("users_ids")
    private List<Integer> userIds;
}

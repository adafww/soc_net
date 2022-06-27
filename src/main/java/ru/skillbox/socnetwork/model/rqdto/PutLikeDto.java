package ru.skillbox.socnetwork.model.rqdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Добавление лайка по идентификатору")
public class PutLikeDto {
    @JsonProperty("item_id")
    Integer itemId;
    String type;
}
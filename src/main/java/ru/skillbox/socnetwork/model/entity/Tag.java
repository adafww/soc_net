package ru.skillbox.socnetwork.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Теги")
public class Tag {
    private Integer id = -1;
    @Schema(example = "Имя тега")
    private String tagName;
}

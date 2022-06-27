package ru.skillbox.socnetwork.model.rsdto.postdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@Schema(description = "Новый пост")
public class NewPostDto {
    String title;
    @JsonProperty("post_text")
    String postText;
    @Schema(minimum = "1", example = "13")
    Integer authorId;
    @Schema(description = "Время создания", example = "1630627200000")
    Long time;
    @Schema(description = "Список тегов")
    List<String> tags;
}
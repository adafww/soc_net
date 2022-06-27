package ru.skillbox.socnetwork.model.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Прикрепленный файл")
public class PostFile {
    private Integer id;
    private Integer postId;
    @Schema(example = "image.jpg")
    private String name;
    @Schema(example = "https://host.ru/image.jpg")
    private String path;
}

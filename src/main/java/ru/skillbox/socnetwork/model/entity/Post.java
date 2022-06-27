package ru.skillbox.socnetwork.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Пост")
public class Post {
    private Integer id;
    @Schema(example = "1630627200000")
    private Long time;
    @Schema(description = "Идентификатор автора поста", example = "13")
    private Integer author;
    @Schema(example = "Самый лучший пост")
    private String title;
    @Schema(example = "Самый лучший текст самого лучшего поста")
    private String postText;
    private Boolean isBlocked = false;
    @Schema(example = "666")
    private Integer likes;
    private Boolean isLiked;
}

package ru.skillbox.socnetwork.model.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Комментарий")
public class PostComment {
    private Integer id;
    @Schema(example = "1630627200000")
    private Long time;
    @Schema(example = "13")
    private Integer postId;
    @Schema(description = "Родительский комментарий", example = "13")
    private Integer parentId;
    @Schema(example = "13")
    private Integer authorId;
    private String commentText;
    private Boolean isBlocked = false;
    private Boolean isLiked;
    @Schema(example = "666")
    private Integer likes;
}

package ru.skillbox.socnetwork.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Schema(description = "Лайк комента")
public class CommentLike {
    @Schema(example = "13")
    private Integer id;
    @Schema(example = "1630627200000")
    private Long time;
    @Schema(example = "13")
    private Integer personId;
    @Schema(example = "13")
    private Integer commentId;
}
package ru.skillbox.socnetwork.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Schema(description = "Лайк поста")
public class PostLike {
    private Integer id;
    @Schema(example = "1630627200000")
    private Long time;
    private Integer personId;
    private Integer postId;
}

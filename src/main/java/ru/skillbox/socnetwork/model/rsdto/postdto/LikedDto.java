package ru.skillbox.socnetwork.model.rsdto.postdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(hidden = true)
public class LikedDto {
    Boolean likes;

    public LikedDto(boolean isLiked) {
        this.likes = isLiked;
    }
}

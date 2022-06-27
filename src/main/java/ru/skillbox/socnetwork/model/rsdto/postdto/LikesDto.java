package ru.skillbox.socnetwork.model.rsdto.postdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(hidden = true)
public class LikesDto {
    Integer likes;
    List<Integer> users;
}

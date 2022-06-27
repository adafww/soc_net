package ru.skillbox.socnetwork.model.rsdto.postdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(hidden = true)
public class TagDto {

    Integer id;
    String tag;
}

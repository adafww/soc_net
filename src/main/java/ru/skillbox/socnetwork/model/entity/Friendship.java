package ru.skillbox.socnetwork.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import liquibase.pro.packaged.F;
import lombok.Data;
import ru.skillbox.socnetwork.model.entity.enums.TypeCode;

@Data
@Schema(description = "Дружба")
public class Friendship {
    private Integer id;
    @Schema(example = "13")
    private Integer srcPersonId;
    @Schema(example = "13")
    private Integer dstPersonId;
    @Schema(example = "1630627200000")
    private Long time;
    @Schema(description = "Статус")
    private TypeCode code;

    public static Friendship getWithIncorrectId() {
        Friendship friendship = new Friendship();
        friendship.setId(-1);
        return friendship;
    }
}

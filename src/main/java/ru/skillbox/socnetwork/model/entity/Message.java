package ru.skillbox.socnetwork.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skillbox.socnetwork.model.entity.enums.TypeReadStatus;

@Data
@AllArgsConstructor
@Schema(description = "Сообщение")
public class Message {

    private Integer id;
    @Schema(example = "1630627200000")
    private Long time;
    private Integer authorId;
    private Integer recipientId;
    @Schema(description = "Текст сообщения")
    private String messageText;
    @Schema(description = "Статус сообщения")
    private TypeReadStatus readStatus;
    @Schema(description = "Идентификатор диалога", example = "13")
    private Integer dialogId;
}

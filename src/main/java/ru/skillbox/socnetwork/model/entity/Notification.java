package ru.skillbox.socnetwork.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.socnetwork.model.entity.enums.TypeNotificationCode;
import ru.skillbox.socnetwork.model.entity.enums.TypeReadStatus;

@Data
@Schema(description = "Оповещение")
public class Notification {
    private Integer id;
    @JsonProperty("notification_type")
    private TypeNotificationCode notificationType;
    @JsonProperty("sent_time")
    @Schema(example = "1630627200000")
    private Long sentTime;
    @JsonProperty("person_id")
    @Schema(description = "Идентификатор пользователя", example = "3")
    private Integer personId;
    @JsonProperty("entity_id")
    @Schema(description = "Идентификатор сущности, относительно которой отправлено оповещение " +
            "(комментарий, друг, пост или сообщение)", example = "3")
    private Integer entityId;
    private Integer distUserId;
    private TypeReadStatus status;
    private String title;
}

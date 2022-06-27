package ru.skillbox.socnetwork.model.rsdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skillbox.socnetwork.model.entity.Notification;
import ru.skillbox.socnetwork.model.entity.enums.TypeNotificationCode;
import ru.skillbox.socnetwork.model.entity.enums.TypeReadStatus;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(hidden = true)
public class NotificationDto {
    private Integer id;
    @JsonProperty("notification_type")
    private TypeNotificationCode notificationType;
    @JsonProperty("sent_time")
    private Long sentTime;
    @JsonProperty("person_id")
    private Integer personId;
    @JsonProperty("entity_id")
    private Integer entityId;
    @JsonProperty("dist_user_id")
    private Integer distUserId;
    private TypeReadStatus status;
    private String title;

    public NotificationDto(TypeNotificationCode notificationType, Long sentTime,
                           Integer personId, Integer entityId, Integer distUserId, TypeReadStatus status, String title) {

        this.notificationType = notificationType;
        this.sentTime = sentTime;
        this.personId = personId;
        this.entityId = entityId;
        this.distUserId = distUserId;
        this.status = status;
        this.title = title;
    }

    public NotificationDto(TypeNotificationCode notificationType, Long sentTime, Integer personId,
                           Integer entityId, String title) {
        this.notificationType = notificationType;
        this.sentTime = sentTime;
        this.personId = personId;
        this.entityId = entityId;
        this.status = TypeReadStatus.SENT;
        this.title = title;
    }


    public NotificationDto(TypeNotificationCode notificationType, Long sentTime, Integer personId, String title) {
        this.notificationType = notificationType;
        this.sentTime = sentTime;
        this.personId = personId;
        this.status = TypeReadStatus.SENT;
        this.title = title;
    }

    public NotificationDto(Notification notification) {
        this.id = notification.getId();
        this.notificationType = notification.getNotificationType();
        this.sentTime = notification.getSentTime();
        this.personId = notification.getPersonId();
        this.entityId = notification.getEntityId();
        this.distUserId = notification.getDistUserId();
        this.status = notification.getStatus();
        this.title = notification.getTitle();
    }
}



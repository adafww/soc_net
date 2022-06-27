package ru.skillbox.socnetwork.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.skillbox.socnetwork.model.entity.enums.TypeNotificationCode;

@Data
public class NotificationSettings {
    private Integer id;
    @JsonProperty("person_id")
    private Integer personId;
    @JsonProperty("type")
    private TypeNotificationCode notificationTypeCode;
    private Boolean enable;

}

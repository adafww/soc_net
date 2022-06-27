package ru.skillbox.socnetwork.model.rsdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skillbox.socnetwork.model.entity.NotificationSettings;
import ru.skillbox.socnetwork.model.entity.enums.TypeNotificationCode;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationSettingsDto {
    //private Integer id;
    //@JsonProperty("person_id")
    //private Integer personId;
    @JsonProperty("type")
    private TypeNotificationCode notificationTypeCode;
    private Boolean enable;

    public NotificationSettingsDto(NotificationSettings notificationSettings) {
//        this.id = notificationSettings.getId();
//        this.personId = notificationSettings.getPersonId();
        this.notificationTypeCode = notificationSettings.getNotificationTypeCode();
        this.enable = notificationSettings.getEnable();
    }
}

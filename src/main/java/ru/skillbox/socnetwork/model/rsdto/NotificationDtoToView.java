package ru.skillbox.socnetwork.model.rsdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skillbox.socnetwork.model.entity.Person;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDtoToView {
    private Integer id;
    private String info;
    @JsonProperty("entity_author")
    private PersonDto entityAuthor;
    @JsonProperty("event_type")
    private String notificationType;
    @JsonProperty("sent_time")
    private Long sentTime;

    public NotificationDtoToView(NotificationDto notificationDto, Person entityAuthor, String info) {

        this.id = notificationDto.getId();
        this.sentTime = notificationDto.getSentTime();
        this.notificationType = notificationDto.getNotificationType().toString();
        this.entityAuthor = new PersonDto(entityAuthor);
        this.info = info;
    }
}

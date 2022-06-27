
package ru.skillbox.socnetwork.model.rsdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.skillbox.socnetwork.model.entity.Person;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonDto {
    private Integer id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("reg_date")
    private Long regDate;
    @JsonProperty("birth_date")
    private Long birthDate;
    private String email;
    private String phone;
    private String photo;
    private String about;
    private String city;
    private String country;
    @JsonProperty("messages_permission")
    private String messagesPermission;
    @JsonProperty("last_online_time")
    private Long lastOnlineTime;
    @JsonProperty("is_blocked")
    private Boolean isBlocked;
    private String token;
    @JsonProperty("is_deleted")
    private Boolean isDeleted;

    public PersonDto(Person person) {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.regDate = person.getRegDate();
        this.birthDate = person.getBirthDate();
        this.email = person.getEmail();
        this.phone = person.getPhone();
        this.photo = person.getPhoto();
        this.about = person.getAbout();
        this.city = person.getCity();
        this.country = person.getCountry();
        this.messagesPermission = person.getMessagesPermission().toString();
        this.lastOnlineTime = person.getLastOnlineTime();
        this.isBlocked = person.getIsBlocked();
        this.isDeleted = person.getIsDeleted();
    }

    public PersonDto(Person person, String token) {
        this(person);
        this.token = token;
    }
}

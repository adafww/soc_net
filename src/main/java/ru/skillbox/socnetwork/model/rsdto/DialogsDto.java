package ru.skillbox.socnetwork.model.rsdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema
public class DialogsDto {
    @Schema(hidden = true)
    private Integer id;
    @Schema(hidden = true)
    private PersonForDialogsDto recipient;
    @JsonProperty("last_message")
    @Schema(hidden = true)
    private MessageDto messageDto;
    @JsonProperty("unread_count")
    @Schema(hidden = true)
    private Integer unreadCount;
    @Schema(hidden = true)
    private Integer count;
    @Schema(example = "ok")
    private String message;

    public DialogsDto(String message) {
        this.message = message;
    }

    public DialogsDto(Integer count) {
        this.count = count;
    }

    public DialogsDto() {

    }
}

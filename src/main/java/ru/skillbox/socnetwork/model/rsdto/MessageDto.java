package ru.skillbox.socnetwork.model.rsdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Сообщение в диалоге")
public class MessageDto {
    private Integer id;
    private PersonForDialogsDto author;
    private PersonForDialogsDto recipient;
    private Long time;
    @JsonProperty("isSentByMe")
    private boolean isSentByMe;
    @JsonProperty("message_text")
    private String messageText;
    @JsonProperty("read_status")
    private String readStatus;
    @JsonProperty("author_id")
    private Integer authorId;
    @JsonProperty("recipient_id")
    private Integer recipientId;

    @JsonProperty("dialog_id")
    private Integer dialogId;

    private String token;

    public MessageDto(Integer id, PersonForDialogsDto author, PersonForDialogsDto recipient, Long time, boolean isSentByMe, String messageText, String readStatus) {
        this.id = id;
        this.author = author;
        this.recipient = recipient;
        this.time = time;
        this.isSentByMe = isSentByMe;
        this.messageText = messageText;
        this.readStatus = readStatus;
    }

    public MessageDto(Integer id, Long time, Integer authorId, Integer recipientId, String messageText, String readStatus) {
        this.id = id;
        this.time = time;
        this.authorId = authorId;
        this.recipientId = recipientId;
        this.messageText = messageText;
        this.readStatus = readStatus;
    }

    public MessageDto() {

    }
}

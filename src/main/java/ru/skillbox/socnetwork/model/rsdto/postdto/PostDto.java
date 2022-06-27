package ru.skillbox.socnetwork.model.rsdto.postdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.socnetwork.model.entity.Post;
import ru.skillbox.socnetwork.model.rsdto.PersonDto;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema
public class PostDto {

    Integer id;
    Long time;
    PersonDto author;
    String title;
    @JsonProperty("post_text")
    String postText;
    Integer likes;
    @JsonProperty("is_blocked")
    Boolean isBlocked;
    List<String> tags;
    List<CommentDto> comments;
    String type;
    @JsonProperty("my_like")
    Boolean isLiked;

    public PostDto(Post post, PersonDto personDto, List<CommentDto> comments, List<String> tags) {
        this.id = post.getId();
        this.time = post.getTime();
        this.author = personDto;
        this.title = post.getTitle();
        this.postText = post.getPostText();
        this.likes = post.getLikes();
        this.isBlocked = post.getIsBlocked();
        this.comments = comments;
        this.tags = tags;
        this.isLiked = post.getIsLiked();
        if (this.time < System.currentTimeMillis()) {
            this.type = "POSTED";
        } else {
            this.type = "QUEUED";
        }
    }

    public PostDto(int id) {
        this.id = id;
    }
}

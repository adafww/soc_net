package ru.skillbox.socnetwork.model.rsdto.postdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skillbox.socnetwork.model.entity.PostComment;
import ru.skillbox.socnetwork.model.rsdto.PersonDto;

import java.util.List;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Комментарий к посту")
public class CommentDto {
    Integer id;
    @JsonProperty("parent_id")
    Integer parentId;
    @JsonProperty("post_id")
    Integer postId;
    @JsonProperty("comment_text")
    String commentText;
    @JsonProperty("my_like")
    Boolean isLiked;
    Integer likes;
    Long time;
    @JsonProperty("author")
    PersonDto author;
    @JsonProperty("is_blocked")
    Boolean isBlocked;
    @JsonProperty("sub_comments")
    List<CommentDto> subComments;

    public CommentDto(PostComment comment, PersonDto personDto) {
        this.id = comment.getId();
        this.parentId = comment.getParentId();
        this.postId = comment.getPostId();
        this.commentText = comment.getCommentText();
        this.time = comment.getTime();
        this.author = personDto;
        this.isBlocked = comment.getIsBlocked();
        this.isLiked = comment.getIsLiked();
        this.likes = comment.getLikes();
    }

    public CommentDto(PostComment comment) {
        this.id = comment.getId();
        this.parentId = comment.getParentId();
        this.postId = comment.getPostId();
        this.commentText = comment.getCommentText();
        this.time = comment.getTime();
        this.isBlocked = comment.getIsBlocked();
        this.isLiked = comment.getIsLiked();
        this.likes = comment.getLikes();
    }
}

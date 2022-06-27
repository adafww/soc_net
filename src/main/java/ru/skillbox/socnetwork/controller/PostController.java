package ru.skillbox.socnetwork.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.socnetwork.exception.ErrorResponseDto;
import ru.skillbox.socnetwork.exception.InvalidRequestException;
import ru.skillbox.socnetwork.logging.InfoLogs;
import ru.skillbox.socnetwork.model.rsdto.postdto.NewPostDto;
import ru.skillbox.socnetwork.model.rsdto.GeneralResponse;
import ru.skillbox.socnetwork.model.rsdto.postdto.PostDto;
import ru.skillbox.socnetwork.service.PostService;
import ru.skillbox.socnetwork.model.rsdto.postdto.CommentDto;


import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
@InfoLogs
@Tag(name="post", description="Взаимодействие с постами")
public class PostController {

    private final PostService postService;

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение поста",
        responses = {
            @ApiResponse(responseCode = "400", description = "Bad request",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                    ))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                    ))),
            @ApiResponse(responseCode = "200", description = "Успешное получение поста",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<PostDto>> getPostById(
            @PathVariable @Parameter(description = "Идентификатор поста") int id)
            throws InvalidRequestException {

        return ResponseEntity.ok(new GeneralResponse<>(postService.getById(id)));
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Удаление поста",
        responses = {
            @ApiResponse(responseCode = "400", description = "Bad request",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                    ))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                    ))),
            @ApiResponse(responseCode = "200", description = "Успешное уаление поста",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<PostDto>> deletePostById(
            @PathVariable @Parameter(description = "Идентификатор поста") int id)
            throws InvalidRequestException {

        postService.deletePostById(id);
        return ResponseEntity.ok(new GeneralResponse<>(new PostDto(id)));
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Редактирование поста",
        responses = {
            @ApiResponse(responseCode = "400", description = "Bad request",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                    ))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                    ))),
            @ApiResponse(responseCode = "200", description = "Успешное редактирование поста",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<PostDto>> editPostById(
            @PathVariable @Parameter(description = "Идентификатор поста") int id,
            @RequestBody NewPostDto newPostDto) throws InvalidRequestException {

        return ResponseEntity.ok(new GeneralResponse<>(postService.editPost(id, newPostDto)));
    }

    @GetMapping(path = "/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение комментариев поста",
        responses = {
            @ApiResponse(responseCode = "400", description = "Bad request",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                    ))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                    ))),
            @ApiResponse(responseCode = "200", description = "Успешное получение комментариев поста",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<List<CommentDto>>> getCommentsByPostId(
            @PathVariable @Parameter(description = "Идентификатор поста") int id) {

        return ResponseEntity.ok(new GeneralResponse<>(postService.getCommentDtoList(id)));
    }

    @Operation(summary = "Добавление комментария к посту",
      responses = {
          @ApiResponse(responseCode = "400", description = "Bad request",
              content = @Content(mediaType = "application/json",
                  array = @ArraySchema(
                      schema = @Schema(implementation = ErrorResponseDto.class)
                  ))),
          @ApiResponse(responseCode = "401", description = "Unauthorized",
              content = @Content(mediaType = "application/json",
                  array = @ArraySchema(
                      schema = @Schema(implementation = ErrorResponseDto.class)
                  ))),
          @ApiResponse(responseCode = "200", description = "Успешное добавление комментария к посту",
              content = @Content(mediaType = "application/json",
                  array = @ArraySchema(
                      schema = @Schema(implementation = GeneralResponse.class)
                  )))
      })
    @PostMapping(path = "/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse<CommentDto>> addCommentToPost(
            @PathVariable int id,
            @RequestBody CommentDto comment)
            throws InvalidRequestException {

        return ResponseEntity.ok(new GeneralResponse<>(postService.addCommentToPost(comment, id)));
    }

    @Operation(summary = "Редактирование комментария к посту",
      responses = {
          @ApiResponse(responseCode = "400", description = "Bad request",
              content = @Content(mediaType = "application/json",
                  array = @ArraySchema(
                      schema = @Schema(implementation = ErrorResponseDto.class)
                  ))),
          @ApiResponse(responseCode = "401", description = "Unauthorized",
              content = @Content(mediaType = "application/json",
                  array = @ArraySchema(
                      schema = @Schema(implementation = ErrorResponseDto.class)
                  ))),
          @ApiResponse(responseCode = "200", description = "Успешное редактирование комментария к посту",
              content = @Content(mediaType = "application/json",
                  array = @ArraySchema(
                      schema = @Schema(implementation = GeneralResponse.class)
                  )))
      })
    @PutMapping(path = "/{id}/comments/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse<CommentDto>> editCommentToPost(
            @PathVariable int id,
            @PathVariable int commentId,
            @RequestBody CommentDto comment) throws InvalidRequestException {

        comment.setId(commentId);
        return ResponseEntity.ok(new GeneralResponse<>(postService.editCommentPost(comment)));
    }

    @Operation(summary = "Удаление комментария к посту",
      responses = {
          @ApiResponse(responseCode = "400", description = "Bad request",
              content = @Content(mediaType = "application/json",
                  array = @ArraySchema(
                      schema = @Schema(implementation = ErrorResponseDto.class)
                  ))),
          @ApiResponse(responseCode = "401", description = "Unauthorized",
              content = @Content(mediaType = "application/json",
                  array = @ArraySchema(
                      schema = @Schema(implementation = ErrorResponseDto.class)
                  ))),
          @ApiResponse(responseCode = "200", description = "Успешное удаление комментария к посту",
              content = @Content(mediaType = "application/json",
                  array = @ArraySchema(
                      schema = @Schema(implementation = GeneralResponse.class)
                  )))
      })
    @DeleteMapping(path = "/{id}/comments/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse<CommentDto>> deleteCommentToPost(
            @PathVariable int id,
            @PathVariable int commentId) throws InvalidRequestException {

        return ResponseEntity.ok(new GeneralResponse<>(postService.deleteCommentPost(commentId)));
    }

    @GetMapping()
    @Operation(summary = "Поиск поста",
        responses = {
            @ApiResponse(responseCode = "400", description = "Bad request",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                    ))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                    ))),
            @ApiResponse(responseCode = "200", description = "Успешное получение поста",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<List<PostDto>>> searchPostByText(
            @RequestParam(value = "text") String text,
            @RequestParam(value = "date_from", defaultValue = "0", required = false) long dateFrom,
            @RequestParam(value = "date_to", defaultValue = "0", required = false) long dateTo,
            @RequestParam(value = "offset", defaultValue = "0", required = false) int offset,
            @RequestParam(value = "author", defaultValue = "", required = false) String author,
            @RequestParam(value = "tags", defaultValue = "", required = false) List<String> tags,
            @RequestParam(value = "perPage", defaultValue = "20", required = false) int perPage) {

        GeneralResponse<List<PostDto>> response = new GeneralResponse<>
                (postService.choosePostsWhichContainsText(text, dateFrom, dateTo, author, tags, perPage));

        return ResponseEntity.ok(response);
    }
}

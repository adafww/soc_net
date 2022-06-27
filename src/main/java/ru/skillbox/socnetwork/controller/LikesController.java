package ru.skillbox.socnetwork.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import ru.skillbox.socnetwork.model.rqdto.PutLikeDto;
import ru.skillbox.socnetwork.model.rsdto.GeneralResponse;
import ru.skillbox.socnetwork.model.rsdto.postdto.LikedDto;
import ru.skillbox.socnetwork.model.rsdto.postdto.LikesDto;
import ru.skillbox.socnetwork.service.LikeService;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@InfoLogs
@Tag(name="likes", description="Взаимодействие с лайками")
public class LikesController {


    private final LikeService likeService;

    @GetMapping(path = "/likes", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение списка лайков",
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
            @ApiResponse(responseCode = "200", description = "Успешное получение списка лайков",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<LikesDto>> getAllLikes(
            @RequestParam(value = "item_id") int itemId,
            @RequestParam(value = "type") String type)
            throws InvalidRequestException {

        return ResponseEntity.ok(new GeneralResponse<>(likeService.getLikes(itemId, type)));
    }

    @PutMapping(path = "/likes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponse<LikesDto>> putAndGetAllLikes(
            @RequestBody PutLikeDto putLikeDto) throws InvalidRequestException {

            return ResponseEntity.ok(new GeneralResponse<>(likeService
                    .putAndGetAllLikes(putLikeDto.getItemId(), putLikeDto.getType())));
    }

    @GetMapping(path = "/liked", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Проверка был ли поставлен лайк пользователем",
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
            @ApiResponse(responseCode = "200", description = "Успешная проверка",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<LikedDto>> getIsLiked(
            @RequestParam(value = "user_id", defaultValue = "0") int userId,
            @RequestParam(value = "item_id") int itemId,
            @RequestParam(value = "type") String type) throws InvalidRequestException {

            return ResponseEntity.ok(new GeneralResponse<>(likeService.getLiked(itemId, type)));
    }


    @DeleteMapping(path = "/likes", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Удаление лайка",
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
            @ApiResponse(responseCode = "200", description = "Успешное удаление лайка",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<LikesDto>> deleteLikeAndGetAllLikes(
            @RequestParam(value = "item_id") int itemId,
            @RequestParam(value = "type") String type) throws InvalidRequestException {

            return ResponseEntity.ok(new GeneralResponse<>(likeService.deleteLike(itemId, type)));
    }
}
package ru.skillbox.socnetwork.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.socnetwork.exception.ErrorResponseDto;
import ru.skillbox.socnetwork.exception.InvalidRequestException;
import ru.skillbox.socnetwork.logging.InfoLogs;
import ru.skillbox.socnetwork.model.rqdto.UserIdsDto;
import ru.skillbox.socnetwork.model.rsdto.*;
import ru.skillbox.socnetwork.service.FriendsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@InfoLogs
@Tag(name="friends", description="Взаимодействие с друзьями")
public class FriendsController {

    private final FriendsService friendsService;

    @DeleteMapping("/friends/request/person/{id}")
    public ResponseEntity<GeneralResponse<DialogsDto>> deleteFriendRequest(@PathVariable Integer id)
            throws InvalidRequestException {

        friendsService.deleteFriendRequestByPersonId(id);
        return ResponseEntity.ok(GeneralResponse.getDefault());
    }

    @GetMapping("/friends/request")
    @Operation(summary = "Получение списка заявок на добавление в друзья",
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
            @ApiResponse(responseCode = "200", description = "Успешное получение списка заявок на добавление в друзья",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<List<PersonDto>>> getListFriendRequests(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "itemPerPage", defaultValue = "20") int itemPerPage) {

        List<PersonDto> personDtoList = friendsService.getListIncomingFriendRequests();
        return ResponseEntity.ok(new GeneralResponse<>(
                personDtoList, personDtoList.size(), offset, itemPerPage));
    }

    @PostMapping("/friends/{id}")
    @Operation(summary = "Добавление пользователя в друзья",
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
            @ApiResponse(responseCode = "200", description = "Успешное добавление пользователя в друзья",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<DialogsDto>> addFriend(
            @PathVariable @Parameter(description = "Идентификатор пользователя") Integer id)
            throws InvalidRequestException {

        friendsService.addFriendById(id);
        return ResponseEntity.ok(GeneralResponse.getDefault());
    }

    @DeleteMapping("/friends/{id}")
    @Operation(summary = "Удаление пользователя из друзей",
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
            @ApiResponse(responseCode = "200", description = "Успешное удаление пользователя из друзей",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<DialogsDto>> deleteFriend(
            @PathVariable @Parameter(description = "Идентификатор пользователя") Integer id)
            throws InvalidRequestException {

        friendsService.deleteFriendById(id);
        return ResponseEntity.ok(GeneralResponse.getDefault());
    }

    @GetMapping("/friends")
    @Operation(summary = "Получение списка друзей",
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
            @ApiResponse(responseCode = "200", description = "Успешное получение списка друзей",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<List<PersonDto>>> getUserFriends(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "itemPerPage", defaultValue = "20") int itemPerPage) {

        List<PersonDto> personDtos = friendsService.getUserFriends();
        return ResponseEntity.ok(new GeneralResponse<>(personDtos, personDtos.size(), offset, itemPerPage));
    }

    @GetMapping("/friends/recommendations")
    @Operation(summary = "Получение списка рекомендаций",
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
            @ApiResponse(responseCode = "200", description = "Успешное получение списка рекомендаций",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<List<PersonDto>>> getListRecommendedFriends(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "itemPerPage", defaultValue = "20") int itemPerPage) {

        List<PersonDto> personDtos = friendsService.getListRecommendedFriends();
        return ResponseEntity.ok(new GeneralResponse<>(personDtos, personDtos.size(), offset, itemPerPage));
    }

    @PostMapping("/is/friends")
    @Operation(summary = "Получить информацию является ли пользователь другом указанных пользователей",
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
            @ApiResponse(responseCode = "200", description = "Успешная регистрация",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<List<FriendshipPersonDto>>> getInformationAboutFriendships(
            @RequestBody UserIdsDto userIdsDto) {

        return ResponseEntity.ok(new GeneralResponse<>(friendsService.getInformationAboutFriendships(userIdsDto)));
    }
}
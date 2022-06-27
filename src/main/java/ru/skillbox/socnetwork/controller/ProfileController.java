package ru.skillbox.socnetwork.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.socnetwork.exception.ErrorResponseDto;
import ru.skillbox.socnetwork.exception.InvalidRequestException;
import ru.skillbox.socnetwork.logging.InfoLogs;
import ru.skillbox.socnetwork.model.entity.Person;
import ru.skillbox.socnetwork.model.rsdto.DialogsDto;
import ru.skillbox.socnetwork.model.rsdto.GeneralResponse;
import ru.skillbox.socnetwork.model.rsdto.PersonDto;
import ru.skillbox.socnetwork.model.rsdto.UpdatePersonDto;
import ru.skillbox.socnetwork.model.rsdto.postdto.NewPostDto;
import ru.skillbox.socnetwork.model.rsdto.postdto.PostDto;
import ru.skillbox.socnetwork.service.PersonService;
import ru.skillbox.socnetwork.service.PostService;

import java.text.ParseException;
import java.util.List;


@Log
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users/")
@InfoLogs
@Tag(name="profile", description="Взаимодействие с профилем")
public class ProfileController {

    private final PersonService personService;
    private final PostService postService;

    @GetMapping(path = "me", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение текущего пользователя",
        responses = {
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                    ))),
            @ApiResponse(responseCode = "200", description = "Успешное получение текущего пользователя",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<PersonDto>> getMyProfile() {

        return ResponseEntity.ok(new GeneralResponse<>(personService.getCurrentPerson(), true));
    }

    @PutMapping(path = "me", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Редактирование текущего пользователя",
        responses = {
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                    ))),
            @ApiResponse(responseCode = "200", description = "Успешное редактирование текущего пользователя",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<PersonDto>> updateProfile(
        @RequestBody UpdatePersonDto updatePersonDto) throws ParseException {

        return ResponseEntity.ok(new GeneralResponse<>(new PersonDto(personService.updatePerson(updatePersonDto))));
    }

    @DeleteMapping(path = "me")
    @Operation(summary = "Удаление пользователя",
        responses = {
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                    ))),
            @ApiResponse(responseCode = "200", description = "Успешное удаление",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<DialogsDto>> deleteProfile()
            throws InvalidRequestException {

        personService.markToDelete();
        return ResponseEntity.ok(GeneralResponse.getDefault());
    }

    @PutMapping(path = "me/return")
    @Operation(summary = "Восстановление пользователя",
        responses = {
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                    ))),
            @ApiResponse(responseCode = "200", description = "Успешное восстановление",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<PersonDto>> returnProfile(){

        return ResponseEntity.ok(new GeneralResponse<>(personService.restoreProfile(), true));
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение пользователя",
        responses = {
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                    ))),
            @ApiResponse(responseCode = "200", description = "Успешное получение пользователя",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<PersonDto>> getProfileById(
            @PathVariable @Parameter(description = "Идентификатор пользователя") int id) {

        return ResponseEntity.ok(new GeneralResponse<>(personService.getPersonDtoById(id), true));
    }

    @GetMapping(path = "{id}/wall", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение записей пользователя",
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
            @ApiResponse(responseCode = "200", description = "Успешное получение записей пользователя",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<Object> getWallByProfileId
            (@PathVariable int id,
             @RequestParam(value = "offset", defaultValue = "0") int offset,
             @RequestParam(value = "itemPerPage", defaultValue = "20") int perPage) {

        return ResponseEntity.ok(new GeneralResponse<>(postService.getWall(id, offset, perPage)));
    }

    @PostMapping(path = "{id}/wall", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Добавление записи на стену пользователя",
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
            @ApiResponse(responseCode = "200", description = "Успешное добавление записи",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<PostDto>> addNewPost
            (@PathVariable int id,
             @RequestParam(value = "publish_date", defaultValue = "-1") long publishDate,
             @RequestBody NewPostDto newPostDto) throws InvalidRequestException {

        newPostDto.setAuthorId(id);
        return ResponseEntity.ok(new GeneralResponse<>(postService.addPost(newPostDto, publishDate)));
    }

    @PutMapping("block/{id}")
    public ResponseEntity<GeneralResponse<DialogsDto>> blockUser(@PathVariable Integer id)
            throws InvalidRequestException {

        personService.blockUser(id);
        return ResponseEntity.ok(GeneralResponse.getDefault());
    }

    @DeleteMapping("block/{id}")
    public ResponseEntity<GeneralResponse<DialogsDto>> unblockUser(@PathVariable Integer id)
            throws InvalidRequestException {

        personService.unblockUser(id);
        return ResponseEntity.ok(GeneralResponse.getDefault());
    }

    @GetMapping("search")
    @Operation(summary = "Поиск пользователя",
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
            @ApiResponse(responseCode = "200", description = "Успешное получение",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = GeneralResponse.class)
                    )))
        })
    public ResponseEntity<GeneralResponse<List<PersonDto>>> searchByPeople(
            @RequestParam(value = "first_name", defaultValue = "", required = false) String firstName,
            @RequestParam(value = "last_name", defaultValue = "", required = false) String lastName,
            @RequestParam(value = "age_from", defaultValue = "0", required = false) long ageFrom,
            @RequestParam(value = "age_to", defaultValue = "150", required = false) long ageTo,
            @RequestParam(value = "country_id", defaultValue = "1", required = false) int countryId,
            @RequestParam(value = "city_id", defaultValue = "1", required = false) int cityId,
            @RequestParam(value = "offset", defaultValue = "0", required = false) int offset,
            @RequestParam(value = "itemPerPage", defaultValue = "20", required = false) int perPage){

            return ResponseEntity.ok(new GeneralResponse<>
                    (personService.getPersonsBySearchParameters(
                            firstName, lastName, ageFrom, ageTo, countryId, cityId, perPage)));
        }
}

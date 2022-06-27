package ru.skillbox.socnetwork.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.socnetwork.exception.ErrorResponseDto;
import ru.skillbox.socnetwork.exception.InvalidRequestException;
import ru.skillbox.socnetwork.logging.InfoLogs;
import ru.skillbox.socnetwork.model.rqdto.LoginDto;
import ru.skillbox.socnetwork.model.rsdto.DialogsDto;
import ru.skillbox.socnetwork.model.rsdto.GeneralResponse;
import ru.skillbox.socnetwork.model.rsdto.PersonDto;
import ru.skillbox.socnetwork.service.PersonService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@InfoLogs
@Tag(name="auth", description="Взаимодействие с авторизацией")
public class AuthController {

    private final PersonService personService;

    @PostMapping(value = "/login")
    @Operation(summary = "Авторизация пользователя",
        responses = {
            @ApiResponse(responseCode = "400", description = "Bad request",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                    ))),
            @ApiResponse(responseCode = "500", description = "Server error"),
            @ApiResponse(responseCode = "200", description = "Успешный вход")
        })
    public ResponseEntity<GeneralResponse<PersonDto>> login(
            @RequestBody LoginDto loginDto) throws InvalidRequestException {

        return ResponseEntity.ok(new GeneralResponse<>(personService.getPersonAfterLogin(loginDto)));
    }

    @PostMapping("/logout")
    @Operation(summary = "Выход пользователя",
        responses = {
            @ApiResponse(responseCode = "400", description = "Bad request",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                    ))),
            @ApiResponse(responseCode = "200", description = "Успешный выход")
        })
    public ResponseEntity<GeneralResponse<DialogsDto>> logout() {

        return ResponseEntity.ok(GeneralResponse.getDefault());
    }
}

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
import ru.skillbox.socnetwork.model.rqdto.CaptchaDto;
import ru.skillbox.socnetwork.model.rqdto.EmailOrPasswordDTO;
import ru.skillbox.socnetwork.model.rqdto.RecoveryDTO;
import ru.skillbox.socnetwork.model.rqdto.RegisterDto;
import ru.skillbox.socnetwork.model.rsdto.DialogsDto;
import ru.skillbox.socnetwork.model.rsdto.GeneralResponse;
import ru.skillbox.socnetwork.model.rsdto.NotificationSettingsDto;
import ru.skillbox.socnetwork.service.NotificationSettingsService;
import ru.skillbox.socnetwork.service.PersonService;
import ru.skillbox.socnetwork.service.CaptchaService;


import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/account")
@InfoLogs
@Tag(name = "account", description = "Взаимодействие с аккаунтом")
public class AccountController {

    private final PersonService personService;
    private final CaptchaService captchaService;
    private final NotificationSettingsService notificationSettingsService;

    @GetMapping("/register")
    public CaptchaDto captcha() {

        return captchaService.returnNewCaptcha();
    }

    @PostMapping(value = "/register")
    @Operation(summary = "Регистрация пользователя",
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
    public ResponseEntity<GeneralResponse<DialogsDto>> register(
            @RequestBody RegisterDto request) throws InvalidRequestException {

        personService.registration(request);
        return ResponseEntity.ok(GeneralResponse.getDefault());
    }

    @PutMapping(value = "/password/recovery")
    @Operation(summary = "Восстановление пароля", description = "Отправляет на email ссылку для восстановления пароля",
            parameters = @Parameter(name = "email", example = "some@mail.ru"),
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
                    @ApiResponse(responseCode = "200",
                            description = "Ссылка на восстановление пароля отправлена на email",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = GeneralResponse.class)
                                    )))
            })
    public ResponseEntity<GeneralResponse<DialogsDto>> recoverPassword(
            @RequestBody RecoveryDTO body) throws InvalidRequestException {

        personService.recoverPassword(body.getEmail());
        return ResponseEntity.ok(GeneralResponse.getDefault());
    }

    @PutMapping(value = "/password/set")
    @Operation(summary = "Изменение пароля",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            schemaProperties = {}
                    )),
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
                    @ApiResponse(responseCode = "200", description = "Успешное изменение пароля",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = GeneralResponse.class)
                                    )))
            })
    public ResponseEntity<GeneralResponse<DialogsDto>> setPassword(
            @RequestBody EmailOrPasswordDTO body) throws InvalidRequestException {

        personService.setPassword(body);
        return ResponseEntity.ok(GeneralResponse.getDefault());
    }

    @PutMapping(value = "/email/recovery")
    @Operation(summary = "Восстановление email", description = "Отправляет на email ссылку для восстановления",
            parameters = @Parameter(name = "email", example = "some@mail.ru"),
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
                    @ApiResponse(responseCode = "200", description = "Ссылка на восстановление отправлена на email",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = GeneralResponse.class)
                                    )))
            })
    public ResponseEntity<GeneralResponse<DialogsDto>> recoverEmail(
            @RequestBody RecoveryDTO body) throws InvalidRequestException {

        personService.recoverEmail(body.getEmail());

        return ResponseEntity.ok(GeneralResponse.getDefault());
    }

    @PutMapping(value = "/email")
    @Operation(summary = "Изменение email",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            schemaProperties = {}
                    )),
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
                    @ApiResponse(responseCode = "200",
                            description = "Ссылка на восстановление пароля отправлена на email",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = GeneralResponse.class)
                                    )))
            })
    public ResponseEntity<GeneralResponse<DialogsDto>> changeEmail(
            @RequestBody EmailOrPasswordDTO body) throws InvalidRequestException {

        personService.updateEmail(body);
        return ResponseEntity.ok(GeneralResponse.getDefault());
    }

    @PutMapping(value = "/notifications")
    public ResponseEntity<GeneralResponse<Object>> changeNotificationSettings(
            @RequestBody Map<String, String> body) {

        String notificationType = body.get("notification_type");
        String enable = body.get("enable");

        notificationSettingsService.changeSettingsToNotification(notificationType, enable);

        return ResponseEntity.ok(new GeneralResponse<Object>());

    }

    @GetMapping(value = "/notifications")
    public ResponseEntity<GeneralResponse<List<NotificationSettingsDto>>> getNotificationSettings() {

        List<NotificationSettingsDto> notificationSettings = notificationSettingsService.getSettingsForUser();
        return ResponseEntity.ok(new GeneralResponse<>(notificationSettings));
    }
}

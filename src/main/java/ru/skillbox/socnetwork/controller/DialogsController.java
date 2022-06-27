package ru.skillbox.socnetwork.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.socnetwork.exception.ErrorResponseDto;
import ru.skillbox.socnetwork.logging.InfoLogs;
import ru.skillbox.socnetwork.model.rqdto.DialogRequest;
import ru.skillbox.socnetwork.model.rqdto.MessageRequest;
import ru.skillbox.socnetwork.model.rsdto.DialogDto;
import ru.skillbox.socnetwork.model.rsdto.DialogsDto;
import ru.skillbox.socnetwork.model.rsdto.GeneralResponse;
import ru.skillbox.socnetwork.model.rsdto.MessageDto;
import ru.skillbox.socnetwork.logging.InfoLogs;
import ru.skillbox.socnetwork.model.rsdto.*;
import ru.skillbox.socnetwork.security.JwtTokenProvider;
import ru.skillbox.socnetwork.security.SecurityUser;
import ru.skillbox.socnetwork.security.UserDetailsServiceImpl;
import ru.skillbox.socnetwork.service.DialogsService;
import ru.skillbox.socnetwork.service.websocket.DialogsServiceWebSocket;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dialogs")
@InfoLogs
@Tag(name="dialogs", description="Взаимодействие с диалогами")
public class DialogsController {

    private final DialogsService dialogsService;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final DialogsServiceWebSocket dialogsServiceWebSocket;

    @GetMapping
    @Operation(summary = "Получение списка диалогов",
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
            @ApiResponse(responseCode = "200", description = "Успешное получение списка диалогов")
        })
    public ResponseEntity<GeneralResponse<List<DialogsDto>>> getDialog() {

        List<DialogsDto> list = dialogsService.getDialogs();
        return ResponseEntity.ok(new GeneralResponse<>(list, list.size()));
    }

    @PostMapping
    @Operation(summary = "Создание диалога",
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
            @ApiResponse(responseCode = "200", description = "Успешное создание диалога")
    })
    public ResponseEntity<GeneralResponse<DialogDto>> createDialog(@RequestBody DialogRequest request) {

        return ResponseEntity.ok(new GeneralResponse<>(dialogsService.createDialog(request.getUserIds()), true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse<DialogDto>> deleteDialog(@PathVariable Integer id) {
        return ResponseEntity.ok(new GeneralResponse<>(dialogsService.deleteDialogByById(id), true));
    }

    @GetMapping("/{id}/messages")
    @Operation(summary = "Получение списка сообщений в диалоге",
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
            @ApiResponse(responseCode = "200", description = "Успешное получение списка сообщений в диалоге")
        })
    public ResponseEntity<GeneralResponse<List<MessageDto>>> getDialogsMessageList(
            @PathVariable @Parameter(description = "Идентификатор диалога") Integer id) {

        List<MessageDto> list = dialogsService.getMessageDtoListByDialogId(id);
        return ResponseEntity.ok(new GeneralResponse<>(list, list.size()));
    }

    @GetMapping(path = "/unreaded", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получение количества непрочитанных сообщений",
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
            @ApiResponse(responseCode = "200", description = "Успешное получение количества непрочитанных сообщений")
        })
    public ResponseEntity<GeneralResponse<DialogsDto>> getUnread() {
        return ResponseEntity.ok(new GeneralResponse<>(dialogsService.getUnreadMessageCount(), true));
    }

    @PostMapping("/{id}/messages")
    @Operation(summary = "Отправка сообщения",
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
            @ApiResponse(responseCode = "200", description = "Успешная отправка сообщения")
        })
    public ResponseEntity<GeneralResponse<MessageDto>> sendMessage(
            @RequestBody MessageRequest messageRequest,
            @PathVariable @Parameter(description = "Идентификатор диалога") Integer id) {) {
        if (!messageRequest.getMessageText().equals("")) {
            return ResponseEntity.ok(dialogsService.sendMessage(messageRequest.getMessageText(), id));
        }
        return ResponseEntity.ok().build();


        return ResponseEntity.ok(new GeneralResponse<>(dialogsService.sendMessage(messageRequest, id), true));
    }

    @MessageMapping("/messages")
    public void message(MessageDto message) {
        GeneralResponse<MessageDto> generalResponse = dialogsServiceWebSocket.sendMessage(
                message.getMessageText(), message.getId(), jwtTokenProvider.getEmailFromToken(message.getToken()));

        messagingTemplate.convertAndSendToUser(String.valueOf(message.getId()),"/messages", generalResponse);
    }

    @MessageMapping("/typing")
    public void typing(MessageDto info) {
        messagingTemplate.convertAndSendToUser(String.valueOf(info.getRecipientId()), "/messages", info);
    }
}

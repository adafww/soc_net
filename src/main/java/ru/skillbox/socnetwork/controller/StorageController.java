package ru.skillbox.socnetwork.controller;

import com.dropbox.core.DbxException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.socnetwork.exception.ErrorResponseDto;
import ru.skillbox.socnetwork.logging.InfoLogs;
import ru.skillbox.socnetwork.model.rsdto.GeneralResponse;
import ru.skillbox.socnetwork.model.rsdto.filedto.FileUploadDTO;
import ru.skillbox.socnetwork.service.storage.StorageService;

@Log
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/storage")
@InfoLogs
@Tag(name="storage", description="Взаимодействие с хранилищем")
public class StorageController {

  private final StorageService storageService;

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Загрузка изображения", description = "Загрузка изображения в хранилище",
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
          @ApiResponse(responseCode = "500", description = "Server error"),
          @ApiResponse(responseCode = "200", description = "Успешная загрузка",
              content = @Content(mediaType = "application/json",
                  array = @ArraySchema(
                      schema = @Schema(implementation = GeneralResponse.class)
                  )))
      })
  public ResponseEntity<GeneralResponse<FileUploadDTO>> uploadImage(@RequestBody MultipartFile file)
      throws IOException, DbxException { //TODO: сделать разметку requestBody

    return ResponseEntity.ok(new GeneralResponse<>(storageService.uploadFile(file), true));
  }

}

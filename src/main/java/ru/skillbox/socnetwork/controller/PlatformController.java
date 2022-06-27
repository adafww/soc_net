package ru.skillbox.socnetwork.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.socnetwork.logging.InfoLogs;
import ru.skillbox.socnetwork.model.rsdto.GeneralResponse;
import ru.skillbox.socnetwork.service.SearchService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/platform/")
@InfoLogs
@Tag(name="platform", description="Взаимодействие с платформой")
public class PlatformController {

    private final SearchService searchService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "languages")
    @Operation(summary = "Получение списка языков")
    public ResponseEntity<GeneralResponse<List<String>>> getLanguages() {
        return ResponseEntity.ok(new GeneralResponse<>( List.of("Русский")));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "cities")
    @Operation(summary = "Получение списка городов")
    public ResponseEntity<GeneralResponse<List<String>>> getCityList() {
        return ResponseEntity.ok(new GeneralResponse<>(searchService.getCityList()));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "countries")
    @Operation(summary = "Получение списка стран")
    public ResponseEntity<GeneralResponse<List<String>>> getCountryList() {
        return ResponseEntity.ok(new GeneralResponse<>(searchService.getCountryList()));
    }
}

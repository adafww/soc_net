package ru.skillbox.socnetwork.model.rsdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.socnetwork.service.Constants;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Schema
public class GeneralResponse<T> {
    @Schema(hidden = true)
    private Integer id;
    @Schema(hidden = true)
    private String title;
    @Schema(hidden = true)
    private String path;
    private String error;
    @JsonProperty("error_description")
    @Schema(hidden = true)
    private String errorDescription;
    @Schema(example = "1630627200000")
    private Long timestamp;
    @Schema(hidden = true)
    private Integer total;
    @Schema(hidden = true)
    private Integer offset;
    @Schema(hidden = true)
    private Integer perPage;
    @Schema(example = "ok")
    private T data;

    public GeneralResponse(String path, String error, String errorDescription, long timestamp) {
        this.path = path;
        this.error = error;
        this.errorDescription = errorDescription;
        this.timestamp = timestamp;
    }

    public GeneralResponse(String error, long timestamp, int total, int offset, int perPage, T data) {
        this.error = error;
        this.timestamp = timestamp;
        this.total = total;
        this.offset = offset;
        this.perPage = perPage;
        this.data = data;
    }

    public GeneralResponse(T data, int total, int offset, int perPage) {
        this.error = Constants.STRING;
        this.timestamp = System.currentTimeMillis();
        this.total = total;
        this.offset = offset;
        this.perPage = perPage;
        this.data = data;
    }

    public GeneralResponse(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public GeneralResponse(String error, long timestamp, T data) {
        this.error = error;
        this.timestamp = timestamp;
        this.data = data;
    }

    public GeneralResponse(T data) {
        this(Constants.STRING, System.currentTimeMillis(), 20, 0, 20, data);
    }

    public GeneralResponse(T data, int total) {
        this(Constants.STRING, System.currentTimeMillis(), total, 0, 20, data);
    }

    public GeneralResponse(T data, boolean isShort) {
        this(Constants.STRING, System.currentTimeMillis(), data);
    }

    public static GeneralResponse<DialogsDto> getDefault() {
        return new GeneralResponse<>("message", System.currentTimeMillis(), new DialogsDto("ok"));
    }
}
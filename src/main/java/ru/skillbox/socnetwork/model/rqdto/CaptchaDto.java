package ru.skillbox.socnetwork.model.rqdto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class CaptchaDto {

    private Long id = System.currentTimeMillis();
    @JsonIgnore
    private String hidden;
    private String image;
}
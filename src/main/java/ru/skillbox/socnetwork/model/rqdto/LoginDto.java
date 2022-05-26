package ru.skillbox.socnetwork.model.rqdto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@Schema()
public class LoginDto {
    @Schema(example = "some@mail.ru")
    private String email;
    @Schema(example = "12345678")
    private String password;

    /**
     *      TODO chek method checkPassword
     */
    public boolean checkPassword (String inDBPassword) {
        return new BCryptPasswordEncoder().matches(this.password, inDBPassword);
    }
}

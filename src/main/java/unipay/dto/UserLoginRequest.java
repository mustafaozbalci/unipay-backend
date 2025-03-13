package unipay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginRequest {

    @NotBlank(message = "Email boş olamaz")
    private String email;

    @NotBlank(message = "Parola boş olamaz")
    private String password;
}

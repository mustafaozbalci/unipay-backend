package unipay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for user login requests.
 * Contains credentials required for authentication.
 */
@Data
public class UserLoginRequest {

    /**
     * Email address of the user.
     * Cannot be blank.
     */
    @NotBlank(message = "Email boş olamaz")
    private String email;

    /**
     * Password of the user.
     * Cannot be blank.
     */
    @NotBlank(message = "Parola boş olamaz")
    private String password;
}

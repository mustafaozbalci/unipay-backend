package unipay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for updating a user's password.
 * Contains the new password to set for the account.
 */
@Data
public class UserPasswordUpdateRequest {

    /**
     * New password for the user.
     * Must not be blank and must be at least 6 characters long.
     */
    @NotBlank(message = "Parola boş olamaz")
    @Size(min = 6, message = "Parola en az 6 karakter olmalıdır")
    private String newPassword;
}

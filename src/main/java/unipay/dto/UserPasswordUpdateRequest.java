package unipay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserPasswordUpdateRequest {

    @NotBlank(message = "Parola boş olamaz")
    @Size(min = 6, message = "Parola en az 6 karakter olmalıdır")
    private String newPassword;
}

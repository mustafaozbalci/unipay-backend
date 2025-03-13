package unipay.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterRequest {

    @NotBlank(message = "Kullanıcı adı boş olamaz")
    private String username;

    @NotBlank(message = "Parola boş olamaz")
    @Size(min = 6, message = "Parola en az 6 karakter olmalıdır")
    private String password;

    @NotBlank(message = "Email boş olamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    private String email;

    @NotBlank(message = "Öğrenci numarası boş olamaz")
    @Size(min = 9, message = "Student Number en az 9 karakter olmalıdır")
    private String studentNumber;
}

package unipay.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request DTO for registering a new user.
 * Contains all required fields for account creation.
 */
@Data
public class UserRegisterRequest {

    /**
     * Desired username for the new account.
     * Must not be blank.
     */
    @NotBlank(message = "Kullanıcı adı boş olamaz")
    private String username;

    /**
     * Password for the new account.
     * Must not be blank and must have at least 6 characters.
     */
    @NotBlank(message = "Parola boş olamaz")
    @Size(min = 6, message = "Parola en az 6 karakter olmalıdır")
    private String password;

    /**
     * Email address for the new user.
     * Must not be blank and must be a valid email format.
     */
    @NotBlank(message = "Email boş olamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    private String email;

    /**
     * Student number to associate with the account.
     * Must not be blank and must have at least 9 characters.
     */
    @NotBlank(message = "Öğrenci numarası boş olamaz")
    @Size(min = 9, message = "Student Number en az 9 karakter olmalıdır")
    private String studentNumber;

    /**
     * Vehicle plate number for campus access.
     * Optional field, but if provided must match Turkish plate format:
     * 2 digits, 1–3 uppercase letters, then 2–3 digits (e.g., 34ABC123).
     */
    @Pattern(regexp = "^\\d{2}[A-Z]{1,3}\\d{2,3}$", message = "Plaka formatı yanlış. 2 rakam + 1–3 harf + 2–3 rakam olmalı (örn: 34ABC123)")
    private String plate;
}

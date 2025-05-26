package unipay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * DTO for updating a user's vehicle plate number.
 * The plate must follow the Turkish format.
 */
@Data
public class UserPlateUpdateRequest {

    /**
     * New vehicle plate number.
     * Must not be blank.
     * Turkish format: 2 digits, 1–3 uppercase letters, 2–3 digits (e.g., 34ABC123).
     */
    @NotBlank(message = "Plate cannot be blank")
    @Pattern(regexp = "^[0-9]{2}[A-Z]{1,3}[0-9]{2,3}$", message = "Plate must follow Turkish format: 2 digits, 1–3 letters, then 2–3 digits (e.g. 34ABC123)")
    private String plate;
}

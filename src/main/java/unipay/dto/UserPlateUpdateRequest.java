// src/main/java/unipay/dto/UserPlateUpdateRequest.java
package unipay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserPlateUpdateRequest {
    @NotBlank(message = "Plate cannot be blank")
    @Pattern(
            regexp = "^[0-9]{2}[A-Z]{1,3}[0-9]{2,3}$",
            message = "Plate must follow Turkish format: 2 digits, 1–3 letters, then 2–3 digits (e.g. 34ABC123)"
    )
    private String plate;
}

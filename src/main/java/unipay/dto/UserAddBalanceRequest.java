package unipay.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Request DTO for adding funds to a user's balance.
 * Contains the amount to be added.
 */
@Data
public class UserAddBalanceRequest {

    /**
     * Amount to add to the user's balance.
     * Must not be null and must be a positive value.
     */
    @NotNull(message = "Eklenecek tutar boş olamaz")
    @Positive(message = "Eklenecek tutar pozitif olmalıdır")
    private Double amountToAdd;
}

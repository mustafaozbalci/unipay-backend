package unipay.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UserAddBalanceRequest {

    @NotNull(message = "Eklenecek tutar boş olamaz")
    @Positive(message = "Eklenecek tutar pozitif olmalıdır")
    private Double amountToAdd;
}

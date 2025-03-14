package unipay.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequest {

    @NotBlank(message = "Ürün adı boş olamaz")
    private String itemName;

    @NotNull(message = "Miktar boş olamaz")
    @Min(value = 1, message = "Miktar en az 1 olmalıdır")
    private Integer quantity;
    @NotNull(message = "Birim fiyat boş olamaz")
    @DecimalMin(value = "0.0", inclusive = true, message = "Birim fiyat negatif olamaz")
    private Double price;
}

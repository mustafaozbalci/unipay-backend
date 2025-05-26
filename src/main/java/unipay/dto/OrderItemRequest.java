package unipay.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request DTO for a single item in an order.
 * Holds details about the product name, quantity, and unit price.
 */
@Data
public class OrderItemRequest {

    /**
     * Name of the product to be ordered.
     */
    @NotBlank(message = "Ürün adı boş olamaz")
    private String itemName;

    /**
     * Number of units to order. Must be at least 1.
     */
    @NotNull(message = "Miktar boş olamaz")
    @Min(value = 1, message = "Miktar en az 1 olmalıdır")
    private Integer quantity;

    /**
     * Price per unit. Cannot be negative.
     */
    @NotNull(message = "Birim fiyat boş olamaz")
    @DecimalMin(value = "0.0", inclusive = true, message = "Birim fiyat negatif olamaz")
    private Double price;
}

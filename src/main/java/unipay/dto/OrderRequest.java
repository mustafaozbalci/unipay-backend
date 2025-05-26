package unipay.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * DTO for placing a new order.
 * Holds the target restaurant ID, the list of items to order,
 * and the total amount for the order.
 */
@Data
public class OrderRequest {

    /**
     * ID of the restaurant where the order will be placed.
     */
    @NotNull(message = "Restoran ID boş olamaz")
    private Long restaurantId;

    /**
     * List of items included in this order.
     * Must contain at least one OrderItemRequest and each entry will be validated.
     */
    @NotEmpty(message = "Sipariş öğeleri boş olamaz")
    @Valid
    private List<OrderItemRequest> items;

    /**
     * Sum total of all item prices in the order.
     * Must be zero or a positive number.
     */
    @NotNull(message = "Toplam tutar boş olamaz")
    @DecimalMin(value = "0.0", inclusive = true, message = "Toplam tutar negatif olamaz")
    private Double totalAmount;
}

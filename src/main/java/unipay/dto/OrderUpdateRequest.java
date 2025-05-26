package unipay.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import unipay.entity.OrderStatus;

/**
 * Request DTO for updating an existing order's status and preparation time.
 */
@Data
public class OrderUpdateRequest {

    /**
     * ID of the order to update.
     */
    @NotNull(message = "Sipariş ID boş olamaz")
    private Long orderId;

    /**
     * New status to assign to the order (e.g., PREPARING, COMPLETED).
     */
    @NotNull(message = "Sipariş durumu boş olamaz")
    private OrderStatus status;

    /**
     * (Optional) Updated estimated preparation time in minutes.
     */
    private Integer estimatedPreparationTime;
}

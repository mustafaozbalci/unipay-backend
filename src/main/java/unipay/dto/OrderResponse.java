package unipay.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO representing the details of an order returned to the client.
 */
@Data
public class OrderResponse {

    /**
     * Unique identifier of the order
     */
    private Long orderId;

    /**
     * Name of the restaurant where the order was placed
     */
    private String restaurantName;

    /**
     * List of items included in the order
     */
    private List<OrderItemResponse> items;

    /**
     * Timestamp when the order was created
     */
    private LocalDateTime orderTime;

    /**
     * Current status of the order (e.g., NEW, PREPARING, COMPLETED)
     */
    private String status;

    /**
     * Total amount charged for the order
     */
    private Double totalAmount;

    /**
     * Estimated preparation time in minutes
     */
    private Integer estimatedPreparationTime;

    /**
     * Username of the customer who placed the order
     */
    private String customerUsername;
}

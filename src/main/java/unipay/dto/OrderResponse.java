package unipay.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long orderId;
    private String restaurantName;
    private List<OrderItemResponse> items;
    private LocalDateTime orderTime;
    private String status;
    private Double totalAmount;
    private Integer estimatedPreparationTime;
    private String customerUsername;
}

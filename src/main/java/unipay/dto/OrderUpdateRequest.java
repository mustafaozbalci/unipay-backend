package unipay.dto;

import unipay.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderUpdateRequest {

    @NotNull(message = "Sipariş ID boş olamaz")
    private Long orderId;

    @NotNull(message = "Sipariş durumu boş olamaz")
    private OrderStatus status;


    private Integer estimatedPreparationTime;
}

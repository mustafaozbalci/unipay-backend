package unipay.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {

    @NotNull(message = "Restoran ID boş olamaz")
    private Long restaurantId;

    @NotEmpty(message = "Sipariş öğeleri boş olamaz")
    @Valid
    private List<OrderItemRequest> items;

    @NotNull(message = "Toplam tutar boş olamaz")
    @DecimalMin(value = "0.0", inclusive = true, message = "Toplam tutar negatif olamaz")
    private Double totalAmount;
}

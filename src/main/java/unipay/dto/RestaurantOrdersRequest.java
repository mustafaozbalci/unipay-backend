package unipay.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RestaurantOrdersRequest {

    @NotNull(message = "Restoran ID boş olamaz")
    private Long restaurantId;
}

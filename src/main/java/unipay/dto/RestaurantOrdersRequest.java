package unipay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestaurantOrdersRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;
}

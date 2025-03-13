package unipay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestaurantCreateRequest {

    @NotBlank(message = "Restoran adı boş olamaz")
    private String name;
}

package unipay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestaurantNameUpdateRequest {
    @NotBlank(message = "Restoran adı boş olamaz")
    private String name;
}

package unipay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request DTO for creating a new restaurant.
 * Holds the name of the restaurant to be added.
 */
@Data
public class RestaurantCreateRequest {

    /**
     * Name of the restaurant.
     * Must not be blank.
     */
    @NotBlank(message = "Restoran adı boş olamaz")
    private String name;
}

package unipay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request DTO for updating an existing restaurant's name.
 * Contains the new name to be applied.
 */
@Data
public class RestaurantNameUpdateRequest {

    /**
     * New name for the restaurant.
     * Cannot be blank.
     */
    @NotBlank(message = "Restoran adı boş olamaz")
    private String name;
}

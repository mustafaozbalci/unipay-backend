package unipay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request DTO for retrieving orders belonging to a specific restaurant.
 * Holds the restaurant's name used to filter orders.
 */
@Data
public class RestaurantOrdersRequest {

    /**
     * Name of the restaurant whose orders should be fetched.
     */
    @NotBlank(message = "Name cannot be empty")
    private String name;
}

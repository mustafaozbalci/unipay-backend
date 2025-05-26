package unipay.dto;

import lombok.Data;

/**
 * DTO for returning restaurant information in API responses.
 * Contains the restaurant's unique identifier and its name.
 */
@Data
public class RestaurantResponse {

    /**
     * Unique identifier of the restaurant.
     */
    private Long id;

    /**
     * Name of the restaurant.
     */
    private String name;
}

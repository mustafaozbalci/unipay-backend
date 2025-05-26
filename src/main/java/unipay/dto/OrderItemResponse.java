package unipay.dto;

import lombok.Data;

/**
 * DTO representing a single item in an order response.
 * Contains the item name, quantity, and price.
 */
@Data
public class OrderItemResponse {

    /**
     * Name of the item.
     */
    private String itemName;

    /**
     * Quantity ordered.
     */
    private Integer quantity;

    /**
     * Price of the item.
     */
    private Double price;
}

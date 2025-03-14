package unipay.dto;

import lombok.Data;

@Data
public class OrderItemResponse {
    private String itemName;
    private Integer quantity;
    private Double price;
}

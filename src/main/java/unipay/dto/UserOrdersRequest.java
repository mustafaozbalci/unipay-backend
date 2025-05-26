package unipay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request DTO for fetching orders of a specific user.
 * Holds the username used to filter the order history.
 */
@Data
public class UserOrdersRequest {

    /**
     * Username of the customer whose orders are being requested.
     * Must not be blank.
     */
    @NotBlank(message = "Kullanıcı adı boş olamaz")
    private String username;
}

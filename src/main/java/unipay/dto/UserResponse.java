package unipay.dto;

import lombok.Data;

/**
 * DTO representing public user information returned by the API.
 */
@Data
public class UserResponse {

    /**
     * Unique identifier of the user
     */
    private Long id;

    /**
     * Username chosen by the user
     */
    private String username;

    /**
     * User's email address
     */
    private String email;

    /**
     * Current account balance
     */
    private Double balance;

    /**
     * Registered vehicle plate number for parking access
     */
    private String plate;
}

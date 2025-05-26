package unipay.dto;

import lombok.Data;

/**
 * Response returned upon successful authentication.
 * Includes the JWT token and the authenticated user's public data.
 */
@Data
public class AuthenticationResponse {

    /**
     * JWT token used for subsequent authenticated requests.
     */
    private String token;

    /**
     * Public-facing user details associated with this session.
     */
    private UserResponse user;

    /**
     * Create a new AuthenticationResponse.
     *
     * @param token the generated JWT token
     * @param user  the authenticated user's DTO
     */
    public AuthenticationResponse(String token, UserResponse user) {
        this.token = token;
        this.user = user;
    }
}

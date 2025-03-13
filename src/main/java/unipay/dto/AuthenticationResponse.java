package unipay.dto;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String token;
    private UserResponse user;

    public AuthenticationResponse(String token, UserResponse user) {
        this.token = token;
        this.user = user;
    }
}

package unipay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserOrdersRequest {

    @NotBlank(message = "Kullanıcı adı boş olamaz")
    private String username;
}

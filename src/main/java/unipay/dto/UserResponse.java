// src/main/java/unipay/dto/UserResponse.java
package unipay.dto;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Double balance;

    // Yeni eklenen plaka alanı
    private String plate;
}

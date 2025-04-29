// src/main/java/unipay/dto/ParkingSessionDto.java
package unipay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSessionDto {
    private Long id;
    private Long parkingAreaId;
    private Long userId;
    private String plate;
    private LocalDateTime enterTime;
    private LocalDateTime exitTime;   // çıkış yapılmadıysa null
    private BigDecimal fee;           // hesaplanmış ücret
}

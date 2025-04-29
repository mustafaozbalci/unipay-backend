// src/main/java/unipay/entity/ParkingSession.java
package unipay.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private ParkingArea parkingArea;

    @ManyToOne(optional = true)
    private User user;            // Kullanıcı bağlıysa

    @Column(nullable = true)
    private String plate;         // Manuel plaka

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime enterTime;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime exitTime;

    @Column(precision = 10, scale = 2)
    private BigDecimal fee;
}

// src/main/java/unipay/entity/ParkingArea.java
package unipay.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ParkingStatus status;

    private String topPercent;
    private String leftPercent;
}

package unipay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a parking area in the system.
 * Contains display coordinates, capacity, and current status.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingArea {

    /**
     * Primary key of the parking area (auto-generated)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Descriptive name of the parking area
     */
    private String name;

    /**
     * Current status of the parking area (e.g., AVAILABLE, FULL, CLOSED)
     */
    @Enumerated(EnumType.STRING)
    private ParkingStatus status;

    /**
     * Maximum number of vehicles that can occupy this area
     */
    @Column(nullable = false)
    private Integer capacity;

    /**
     * Top position percentage for UI map rendering (e.g., "78%")
     */
    private String topPercent;

    /**
     * Left position percentage for UI map rendering (e.g., "12%")
     */
    private String leftPercent;
}

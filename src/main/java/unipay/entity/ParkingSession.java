package unipay.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a parking session for a vehicle.
 * Tracks entry and exit times, associated area/user, and calculated fee.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSession {

    /**
     * Primary key of the parking session (auto-generated).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Parking area where this session took place.
     */
    @ManyToOne(optional = false)
    private ParkingArea parkingArea;

    /**
     * User who started the session, if authenticated.
     */
    @ManyToOne(optional = true)
    private User user;

    /**
     * License plate entered manually when no user is linked.
     */
    @Column(nullable = true)
    private String plate;

    /**
     * Time when the vehicle entered.
     * Formatted as dd-MM-yyyy HH:mm:ss in JSON.
     */
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime enterTime;

    /**
     * Time when the vehicle exited; null if still active.
     * Formatted as dd-MM-yyyy HH:mm:ss in JSON.
     */
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime exitTime;

    /**
     * Fee calculated for this session.
     * Stored with precision 10 and scale 2.
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal fee;
}

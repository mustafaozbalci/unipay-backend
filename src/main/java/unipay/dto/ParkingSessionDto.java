package unipay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for parking session details.
 * Contains session ID, area, user, timestamps, and calculated fee.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSessionDto {

    /**
     * Unique identifier of the parking session
     */
    private Long id;

    /**
     * Identifier of the associated parking area
     */
    private Long parkingAreaId;

    /**
     * Identifier of the user who started the session
     */
    private Long userId;

    /**
     * Vehicle license plate for this session
     */
    private String plate;

    /**
     * Time when the vehicle entered the parking area
     */
    private LocalDateTime enterTime;

    /**
     * Time when the vehicle exited; null if the session is still active
     */
    private LocalDateTime exitTime;

    /**
     * Calculated fee for the session
     */
    private BigDecimal fee;
}

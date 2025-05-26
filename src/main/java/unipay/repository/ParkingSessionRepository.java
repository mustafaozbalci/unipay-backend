package unipay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unipay.entity.ParkingArea;
import unipay.entity.ParkingSession;

import java.util.List;

/**
 * Repository for managing ParkingSession entities.
 * Provides methods to count active sessions and fetch session histories.
 */
@Repository
public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {

    /**
     * Counts the number of active sessions (exitTime is null) for the given parking area.
     *
     * @param parkingArea the parking area to count active sessions in
     * @return the count of active parking sessions
     */
    int countByParkingAreaAndExitTimeIsNull(ParkingArea parkingArea);

    /**
     * Retrieves all past sessions for a specific user, ordered by enter time descending.
     *
     * @param userId the ID of the user
     * @return list of parking sessions, most recent first
     */
    List<ParkingSession> findByUser_IdOrderByEnterTimeDesc(Long userId);

    /**
     * Retrieves all parking sessions, ordered by enter time descending (most recent first).
     *
     * @return list of all parking sessions
     */
    List<ParkingSession> findAllByOrderByEnterTimeDesc();
}

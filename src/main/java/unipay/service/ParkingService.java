package unipay.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unipay.entity.ParkingArea;
import unipay.entity.ParkingSession;
import unipay.entity.ParkingStatus;
import unipay.repository.ParkingAreaRepository;
import unipay.repository.ParkingSessionRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for handling parking operations.
 * <p>
 * It supports vehicle entry, exit processing, fee calculation,
 * and retrieval of parking session histories.
 */
@Service
public class ParkingService {

    private static final Logger logger = LoggerFactory.getLogger(ParkingService.class);

    private final ParkingAreaRepository parkingAreaRepo;
    private final ParkingSessionRepository sessionRepo;
    private final UserService userService;

    /**
     * Constructs the ParkingService with required repositories and services.
     *
     * @param parkingAreaRepo repository to manage ParkingArea entities
     * @param sessionRepo     repository to manage ParkingSession entities
     * @param userService     service to adjust user balances
     */
    public ParkingService(ParkingAreaRepository parkingAreaRepo, ParkingSessionRepository sessionRepo, UserService userService) {
        this.parkingAreaRepo = parkingAreaRepo;
        this.sessionRepo = sessionRepo;
        this.userService = userService;
    }

    /**
     * Creates a new parking session for a vehicle entering an area.
     * <p>
     * It checks capacity, throws if full, records the session,
     * and marks the area FULL if it reaches capacity.
     *
     * @param areaId      the ID of the parking area
     * @param userId      the ID of the authenticated user (or null)
     * @param manualPlate optional plate string if user is not authenticated
     * @return the persisted ParkingSession
     * @throws IllegalArgumentException if the area ID is invalid
     * @throws IllegalStateException    if the parking area is already full
     */
    @Transactional
    public ParkingSession enterVehicle(Long areaId, Long userId, String manualPlate) {
        logger.info("Vehicle entry: areaId={} userId={}", areaId, userId);
        ParkingArea area = parkingAreaRepo.findById(areaId).orElseThrow(() -> new IllegalArgumentException("Invalid area: " + areaId));

        int occupied = sessionRepo.countByParkingAreaAndExitTimeIsNull(area);
        if (occupied >= area.getCapacity()) {
            throw new IllegalStateException("Parking is full");
        }

        ParkingSession session = new ParkingSession();
        session.setParkingArea(area);

        if (userId != null) {
            // use registered user plate
            session.setUser(userService.getUserById(userId));
            session.setPlate(session.getUser().getPlate());
        } else {
            // manual entry for guests
            session.setPlate(manualPlate.trim().toUpperCase());
        }

        session.setEnterTime(LocalDateTime.now());
        session = sessionRepo.save(session);

        // if this entry fills the area, update status
        if (occupied + 1 >= area.getCapacity()) {
            area.setStatus(ParkingStatus.FULL);
            parkingAreaRepo.save(area);
        }

        logger.info("Created session id={}", session.getId());
        return session;
    }

    /**
     * Completes a parking session when a vehicle exits.
     * <p>
     * It sets the exit time, calculates the fee, deducts or credits balances,
     * and re-opens the area if it was full and space freed up.
     *
     * @param sessionId the ID of the parking session to close
     * @return the updated ParkingSession with exitTime and fee
     * @throws IllegalArgumentException if the session ID is invalid
     * @throws IllegalStateException    if the session was already closed
     */
    @Transactional
    public ParkingSession exitVehicle(Long sessionId) {
        logger.info("Processing exit for sessionId={}", sessionId);
        ParkingSession session = sessionRepo.findById(sessionId).orElseThrow(() -> new IllegalArgumentException("Invalid session: " + sessionId));

        if (session.getExitTime() != null) {
            throw new IllegalStateException("Already exited: " + sessionId);
        }

        session.setExitTime(LocalDateTime.now());
        long minutes = Duration.between(session.getEnterTime(), session.getExitTime()).toMinutes();
        BigDecimal fee = calculateFee(minutes);
        session.setFee(fee);
        session = sessionRepo.save(session);

        if (session.getUser() != null) {
            // deduct from user, credit parking service
            userService.checkAndDecreaseBalance(session.getUser().getId(), fee.doubleValue());
            userService.updateUserBalance("otopark", fee.doubleValue());
        } else {
            // guest sessions go directly to parking service
            userService.updateUserBalance("otopark", fee.doubleValue());
        }

        ParkingArea area = session.getParkingArea();
        int stillOccupied = sessionRepo.countByParkingAreaAndExitTimeIsNull(area);
        if (area.getStatus() == ParkingStatus.FULL && stillOccupied < area.getCapacity()) {
            area.setStatus(ParkingStatus.AVAILABLE);
            parkingAreaRepo.save(area);
        }

        logger.info("Exit completed for sessionId={} fee={}", sessionId, fee);
        return session;
    }

    /**
     * Retrieves past parking sessions for a specific user, ordered newest first.
     *
     * @param userId the ID of the user
     * @return list of ParkingSession entities
     */
    @Transactional(readOnly = true)
    public List<ParkingSession> getUserHistory(Long userId) {
        logger.info("getUserHistory() - Start: userId={}", userId);
        userService.getUserById(userId);
        List<ParkingSession> sessions = sessionRepo.findByUser_IdOrderByEnterTimeDesc(userId);
        logger.info("getUserHistory() - End: returned {} sessions", sessions.size());
        return sessions;
    }

    /**
     * Retrieves all parking sessions (for admin), ordered newest first.
     *
     * @param adminId the ID of the admin user requesting data
     * @return list of ParkingSession entities
     */
    @Transactional(readOnly = true)
    public List<ParkingSession> getAllSessionsForAdmin(Long adminId) {
        logger.info("Listing all sessions for adminId={}", adminId);
        userService.getUserById(adminId);
        List<ParkingSession> list = sessionRepo.findAllByOrderByEnterTimeDesc();
        logger.debug("Total sessions: {}", list.size());
        return list;
    }

    /**
     * Calculates the fee based on duration in minutes.
     * Rounds up to next 0.01 hour and multiplies by rate.
     *
     * @param minutes total parked minutes
     * @return calculated fee as BigDecimal
     */
    private BigDecimal calculateFee(long minutes) {
        BigDecimal ratePerHour = BigDecimal.valueOf(50);
        BigDecimal hours = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_UP);
        return ratePerHour.multiply(hours);
    }

    /**
     * Returns the current fee for an active session,
     * or the finalized fee if already exited.
     *
     * @param sessionId the ID of the parking session
     * @return current or final fee
     * @throws IllegalArgumentException if the session ID is invalid
     */
    @Transactional(readOnly = true)
    public BigDecimal getCurrentFee(Long sessionId) {
        ParkingSession session = sessionRepo.findById(sessionId).orElseThrow(() -> new IllegalArgumentException("Invalid session: " + sessionId));

        if (session.getExitTime() != null) {
            return session.getFee();
        }
        long minutes = Duration.between(session.getEnterTime(), LocalDateTime.now()).toMinutes();
        return calculateFee(minutes);
    }
}

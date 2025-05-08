// src/main/java/unipay/service/ParkingService.java
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

@Service
public class ParkingService {

    private static final Logger logger = LoggerFactory.getLogger(ParkingService.class);

    private final ParkingAreaRepository parkingAreaRepo;
    private final ParkingSessionRepository sessionRepo;
    private final UserService userService;

    public ParkingService(ParkingAreaRepository parkingAreaRepo, ParkingSessionRepository sessionRepo, UserService userService) {
        this.parkingAreaRepo = parkingAreaRepo;
        this.sessionRepo = sessionRepo;
        this.userService = userService;
    }

    @Transactional
    public ParkingSession enterVehicle(Long areaId, Long userId, String manualPlate) {
        logger.info("Vehicle entry: areaId={} userId={}", areaId, userId);
        ParkingArea area = parkingAreaRepo.findById(areaId).orElseThrow(() -> new IllegalArgumentException("Invalid area: " + areaId));

        int occ = sessionRepo.countByParkingAreaAndExitTimeIsNull(area);
        if (occ >= area.getCapacity()) {
            throw new IllegalStateException("Parking is full");
        }

        ParkingSession sess = new ParkingSession();
        sess.setParkingArea(area);
        if (userId != null) {
            sess.setUser(userService.getUserById(userId));
            sess.setPlate(sess.getUser().getPlate());
        } else {
            sess.setPlate(manualPlate.trim().toUpperCase());
        }
        sess.setEnterTime(LocalDateTime.now());
        sess = sessionRepo.save(sess);

        if (occ + 1 >= area.getCapacity()) {
            area.setStatus(ParkingStatus.FULL);
            parkingAreaRepo.save(area);
        }

        logger.info("Created session id={}", sess.getId());
        return sess;
    }

    @Transactional
    public ParkingSession exitVehicle(Long sessionId) {
        logger.info("Processing exit for sessionId={}", sessionId);
        ParkingSession sess = sessionRepo.findById(sessionId).orElseThrow(() -> new IllegalArgumentException("Invalid session: " + sessionId));

        if (sess.getExitTime() != null) {
            throw new IllegalStateException("Already exited: " + sessionId);
        }

        sess.setExitTime(LocalDateTime.now());
        long mins = Duration.between(sess.getEnterTime(), sess.getExitTime()).toMinutes();
        BigDecimal fee = calculateFee(mins);
        sess.setFee(fee);
        sess = sessionRepo.save(sess);

        if (sess.getUser() != null) {
            // 1) Kullanıcı bakiyesini düşür
            userService.checkAndDecreaseBalance(sess.getUser().getId(), fee.doubleValue());
            // 2) Otopark hesabına ekle
            userService.updateUserBalance("otopark", fee.doubleValue());
        } else {
            // manuel girişler direkt otoparka yazılır
            userService.updateUserBalance("otopark", fee.doubleValue());
        }

        ParkingArea area = sess.getParkingArea();
        int occAfter = sessionRepo.countByParkingAreaAndExitTimeIsNull(area);
        if (area.getStatus() == ParkingStatus.FULL && occAfter < area.getCapacity()) {
            area.setStatus(ParkingStatus.AVAILABLE);
            parkingAreaRepo.save(area);
        }

        logger.info("Exit completed for sessionId={} fee={}", sessionId, fee);
        return sess;
    }

    @Transactional(readOnly = true)
    public List<ParkingSession> getUserHistory(Long userId) {
        logger.info("getUserHistory() - Start: userId={}", userId);
        userService.getUserById(userId);
        List<ParkingSession> sessions = sessionRepo.findByUser_IdOrderByEnterTimeDesc(userId);
        logger.info("getUserHistory() - End: returned {} sessions", sessions.size());
        return sessions;
    }

    @Transactional(readOnly = true)
    public List<ParkingSession> getAllSessionsForAdmin(Long adminId) {
        logger.info("Listing all sessions for adminId={}", adminId);
        userService.getUserById(adminId);
        List<ParkingSession> list = sessionRepo.findAllByOrderByEnterTimeDesc();
        logger.debug("Total sessions: {}", list.size());
        return list;
    }

    private BigDecimal calculateFee(long minutes) {
        BigDecimal rate = BigDecimal.valueOf(50);
        BigDecimal hrs = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_UP);
        return rate.multiply(hrs);
    }

    @Transactional(readOnly = true)
    public BigDecimal getCurrentFee(Long sessionId) {
        ParkingSession sess = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid session: " + sessionId));

        if (sess.getExitTime() != null) {
            return sess.getFee();
        }
        long minutes = Duration.between(sess.getEnterTime(), LocalDateTime.now()).toMinutes();
        return calculateFee(minutes);
    }
}

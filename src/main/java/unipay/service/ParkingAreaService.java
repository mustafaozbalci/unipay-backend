package unipay.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unipay.entity.ParkingArea;
import unipay.entity.ParkingStatus;
import unipay.repository.ParkingAreaRepository;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

/**
 * Service for managing parking area statuses.
 * Handles manual updates, scheduled open/close events, and initial status on startup.
 */
@Service
public class ParkingAreaService {

    private static final Logger logger = LoggerFactory.getLogger(ParkingAreaService.class);
    private static final ZoneId ZONE = ZoneId.of("Europe/Istanbul");
    private static final LocalTime CLOSE = LocalTime.of(22, 0);
    private static final LocalTime OPEN = LocalTime.of(5, 0);

    private final ParkingAreaRepository repository;

    /**
     * Construct the service with the required repository.
     *
     * @param repository repository to access ParkingArea entities
     */
    public ParkingAreaService(ParkingAreaRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieve all parking areas from the database.
     *
     * @return list of all ParkingArea entities
     */
    public List<ParkingArea> getAll() {
        return repository.findAll();
    }

    /**
     * Update the status of a specific parking area by ID.
     *
     * @param id     identifier of the parking area to update
     * @param status new status value (e.g. "FULL", "AVAILABLE", "CLOSED")
     * @return the updated ParkingArea entity
     * @throws IllegalArgumentException if no area exists with the given ID
     */
    public ParkingArea updateStatus(Long id, String status) {
        logger.info("Updating status of areaId={} to {}", id, status);
        ParkingArea area = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid area: " + id));
        area.setStatus(ParkingStatus.valueOf(status));
        return repository.save(area);
    }

    /**
     * Scheduled task to close all parking areas at 22:00 Europe/Istanbul daily.
     */
    @Scheduled(cron = "0 0 22 * * *", zone = "Europe/Istanbul")
    @Transactional
    public void closeAllParkingAreas() {
        logger.info("Closing all parking areas");
        repository.findAll().forEach(a -> a.setStatus(ParkingStatus.CLOSED));
    }

    /**
     * Scheduled task to open all parking areas at 05:00 Europe/Istanbul daily.
     */
    @Scheduled(cron = "0 0 5 * * *", zone = "Europe/Istanbul")
    @Transactional
    public void openAllParkingAreas() {
        logger.info("Opening all parking areas");
        repository.findAll().forEach(a -> a.setStatus(ParkingStatus.AVAILABLE));
    }

    /**
     * On application startup, set each area to CLOSED if current time is outside
     * operating hours (05:00–22:00), otherwise AVAILABLE.
     */
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void applyInitialStatus() {
        LocalTime now = LocalTime.now(ZONE);
        logger.info("Applying initial status at {}", now);
        List<ParkingArea> areas = repository.findAll();
        ParkingStatus status = (now.isAfter(CLOSE) || now.isBefore(OPEN)) ? ParkingStatus.CLOSED : ParkingStatus.AVAILABLE;
        areas.forEach(a -> a.setStatus(status));
        repository.saveAll(areas);
    }
}

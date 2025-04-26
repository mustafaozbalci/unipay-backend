package unipay.service;

import jakarta.transaction.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import unipay.entity.ParkingArea;
import unipay.entity.ParkingStatus;
import unipay.repository.ParkingAreaRepository;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingAreaService {

    private static final ZoneId ZONE = ZoneId.of("Europe/Istanbul");
    private static final LocalTime CLOSE_TIME = LocalTime.of(22, 0);
    private static final LocalTime OPEN_TIME = LocalTime.of(5, 0);

    private final ParkingAreaRepository repository;

    public ParkingAreaService(ParkingAreaRepository repository) {
        this.repository = repository;
    }

    public List<ParkingArea> getAll() {
        return repository.findAll();
    }

    public Optional<ParkingArea> updateStatus(Long id, String newStatus) {
        Optional<ParkingArea> areaOpt = repository.findById(id);
        areaOpt.ifPresent(area -> {
            area.setStatus(Enum.valueOf(ParkingStatus.class, newStatus));
            repository.save(area);
        });
        return areaOpt;
    }

    @Scheduled(cron = "0 0 22 * * *", zone = "Europe/Istanbul")
    @Transactional
    public void closeAllParkingAreas() {
        List<ParkingArea> areas = repository.findAll();
        areas.forEach(area -> area.setStatus(ParkingStatus.CLOSED));
        repository.saveAll(areas);
    }

    @Scheduled(cron = "0 0 5 * * *", zone = "Europe/Istanbul")
    @Transactional
    public void openAllParkingAreas() {
        List<ParkingArea> areas = repository.findAll();
        areas.forEach(area -> area.setStatus(ParkingStatus.AVAILABLE));
        repository.saveAll(areas);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void applyInitialStatus() {
        LocalTime now = LocalTime.now(ZONE);
        List<ParkingArea> areas = repository.findAll();
        if (now.isAfter(CLOSE_TIME) || now.isBefore(OPEN_TIME)) {
            areas.forEach(area -> area.setStatus(ParkingStatus.CLOSED));
        } else {
            areas.forEach(area -> area.setStatus(ParkingStatus.AVAILABLE));
        }
        repository.saveAll(areas);
    }
}

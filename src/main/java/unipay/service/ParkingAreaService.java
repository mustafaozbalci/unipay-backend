// src/main/java/unipay/service/ParkingAreaService.java
package unipay.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import unipay.entity.ParkingArea;
import unipay.entity.ParkingStatus;
import unipay.repository.ParkingAreaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ParkingAreaService {

    @Autowired
    private ParkingAreaRepository repository;

    public List<ParkingArea> getAll() {
        return repository.findAll();
    }

    public Optional<ParkingArea> updateStatus(Long id, String newStatus) {
        Optional<ParkingArea> areaOpt = repository.findById(id);
        areaOpt.ifPresent(area -> {
            area.setStatus(Enum.valueOf(unipay.entity.ParkingStatus.class, newStatus));
            repository.save(area);
        });
        return areaOpt;
    }
    // Her gece saat 22:00'de çalışır → tüm otoparkları KAPALI yap
    @Scheduled(cron = "0 0 22 * * *", zone = "Europe/Istanbul")
    @Transactional
    public void closeAllParkingAreas() {
        repository.findAll().forEach(area -> {
            area.setStatus(ParkingStatus.CLOSED);
            repository.save(area);
        });
    }

    // Her sabah saat 05:00'te çalışır → tüm otoparkları AÇIK yap
    @Scheduled(cron = "0 0 5 * * *", zone = "Europe/Istanbul")
    @Transactional
    public void openAllParkingAreas() {
        repository.findAll().forEach(area -> {
            area.setStatus(ParkingStatus.AVAILABLE);
            repository.save(area);
        });
    }
}

// src/main/java/unipay/service/ParkingAreaService.java
package unipay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipay.entity.ParkingArea;
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
}

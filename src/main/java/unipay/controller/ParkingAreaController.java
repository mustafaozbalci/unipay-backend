// src/main/java/unipay/controller/ParkingAreaController.java
package unipay.controller;

import org.springframework.web.bind.annotation.*;
import unipay.entity.ParkingArea;
import unipay.service.ParkingAreaService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/parking-areas")
public class ParkingAreaController {


    private final ParkingAreaService service;

    public ParkingAreaController(ParkingAreaService service) {
        this.service = service;
    }

    @GetMapping
    public List<ParkingArea> getAllAreas() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public Optional<ParkingArea> updateStatus(@PathVariable Long id, @RequestBody String newStatus // örn: "FULL"
    ) {
        return service.updateStatus(id, newStatus.replace("\"", "")); // JSON string temizliği
    }
}

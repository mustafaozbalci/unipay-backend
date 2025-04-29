// src/main/java/unipay/controller/ParkingAreaController.java
package unipay.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unipay.entity.ParkingArea;
import unipay.service.ParkingAreaService;

import java.util.List;

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
    public ResponseEntity<ParkingArea> updateStatus(@PathVariable Long id, @RequestBody String newStatus  // örn: "FULL"
    ) {
        ParkingArea updated = service.updateStatus(id, newStatus.replace("\"", ""));
        return ResponseEntity.ok(updated);
    }
}

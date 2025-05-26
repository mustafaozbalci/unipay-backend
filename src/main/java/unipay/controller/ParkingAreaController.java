package unipay.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unipay.entity.ParkingArea;
import unipay.service.ParkingAreaService;

import java.util.List;

/**
 * REST controller for managing parking areas.
 * <p>
 * Provides endpoints to:
 * - Retrieve all parking areas
 * - Update the status of a specific parking area
 */
@RestController
@RequestMapping("/api/parking-areas")
public class ParkingAreaController {

    private final ParkingAreaService service;

    /**
     * Constructs a new ParkingAreaController with the given service.
     *
     * @param service the service handling parking area operations
     */
    public ParkingAreaController(ParkingAreaService service) {
        this.service = service;
    }

    /**
     * Retrieves all parking areas.
     *
     * @return a list of ParkingArea entities
     */
    @GetMapping
    public List<ParkingArea> getAllAreas() {
        return service.getAll();
    }

    /**
     * Updates the status of the parking area with the given ID.
     *
     * @param id        the ID of the parking area to update
     * @param newStatus the new status value (e.g., "FULL","AVAILABLE","CLOSED"); quotes will be stripped
     * @return the updated ParkingArea wrapped in a ResponseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<ParkingArea> updateStatus(@PathVariable Long id, @RequestBody String newStatus) {
        // Strip any surrounding quotation marks from the request body
        String sanitizedStatus = newStatus.replace("\"", "");
        ParkingArea updated = service.updateStatus(id, sanitizedStatus);
        return ResponseEntity.ok(updated);
    }
}

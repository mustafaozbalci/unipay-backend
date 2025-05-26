package unipay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unipay.entity.ParkingArea;

/**
 * Repository interface for ParkingArea entities.
 * Extends JpaRepository to provide standard CRUD operations
 * and pagination support for ParkingArea.
 */
public interface ParkingAreaRepository extends JpaRepository<ParkingArea, Long> {
    // No custom methods required; default JpaRepository methods are sufficient.
}

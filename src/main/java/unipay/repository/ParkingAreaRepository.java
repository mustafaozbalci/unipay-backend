// src/main/java/unipay/repository/ParkingAreaRepository.java
package unipay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unipay.entity.ParkingArea;

public interface ParkingAreaRepository extends JpaRepository<ParkingArea, Long> {
}

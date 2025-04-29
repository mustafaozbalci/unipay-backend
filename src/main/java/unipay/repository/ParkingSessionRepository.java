// src/main/java/unipay/repository/ParkingSessionRepository.java
package unipay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unipay.entity.ParkingArea;
import unipay.entity.ParkingSession;

import java.util.List;

@Repository
public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {
    // Hâlâ alanda çıkışı yapılmamış oturumların sayısı
    int countByParkingAreaAndExitTimeIsNull(ParkingArea parkingArea);

    // Kullanıcıya ait geçmiş oturumlar — nested property üzerinden
    List<ParkingSession> findByUser_IdOrderByEnterTimeDesc(Long userId);

    // Admin için tüm oturumlar en yeni önce
    List<ParkingSession> findAllByOrderByEnterTimeDesc();
}

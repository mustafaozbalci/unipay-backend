package unipay.repository;

import unipay.entity.Order;
import unipay.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    List<Order> findByRestaurantId(Long restaurantId);

    List<Order> findByStatus(OrderStatus status); // Sipariş durumuna göre sorgu

    List<Order> findByUserIdAndOrderTimeAfter(Long userId, LocalDateTime orderTime); // Tarihe göre sorgu
}

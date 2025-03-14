package unipay.repository;

import unipay.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Belirli bir siparişe ait tüm öğeleri getirir
    List<OrderItem> findByOrderId(Long orderId);

    // Belirli bir ürün adına göre sipariş öğelerini getirir
    List<OrderItem> findByItemNameContainingIgnoreCase(String itemName);

    // Belirli bir restoranın tüm sipariş öğelerini getirir
    List<OrderItem> findByOrder_RestaurantId(Long restaurantId);
}

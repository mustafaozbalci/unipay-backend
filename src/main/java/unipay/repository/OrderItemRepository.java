package unipay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unipay.entity.OrderItem;

import java.util.List;

/**
 * Repository for managing OrderItem entities.
 * Extends JpaRepository to provide basic CRUD operations.
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Retrieves all order items for the given order ID.
     *
     * @param orderId ID of the order
     * @return list of OrderItem entities belonging to the order
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * Retrieves all order items whose names contain the specified text (case-insensitive).
     *
     * @param itemName substring to match within item names
     * @return list of matching OrderItem entities
     */
    List<OrderItem> findByItemNameContainingIgnoreCase(String itemName);

    /**
     * Retrieves all order items for orders placed at a specific restaurant.
     *
     * @param restaurantId ID of the restaurant
     * @return list of OrderItem entities associated with that restaurant
     */
    List<OrderItem> findByOrder_RestaurantId(Long restaurantId);
}

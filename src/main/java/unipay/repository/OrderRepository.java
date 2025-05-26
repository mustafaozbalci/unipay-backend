package unipay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unipay.entity.Order;
import unipay.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for managing Order entities.
 * Extends JpaRepository to provide CRUD operations and custom queries.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Retrieves all orders placed by the specified user.
     *
     * @param userId the ID of the user
     * @return list of orders for the given user
     */
    List<Order> findByUserId(Long userId);

    /**
     * Retrieves all orders associated with the specified restaurant.
     *
     * @param restaurantId the ID of the restaurant
     * @return list of orders for the given restaurant
     */
    List<Order> findByRestaurantId(Long restaurantId);

    /**
     * Retrieves all orders matching the given status.
     *
     * @param status the status to filter orders by
     * @return list of orders with the specified status
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * Retrieves all orders for a user placed after the specified timestamp.
     *
     * @param userId    the ID of the user
     * @param orderTime the lower bound for the order time
     * @return list of orders placed after the given time
     */
    List<Order> findByUserIdAndOrderTimeAfter(Long userId, LocalDateTime orderTime);
}

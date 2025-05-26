package unipay.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unipay.dto.OrderRequest;
import unipay.dto.OrderResponse;
import unipay.dto.OrderUpdateRequest;
import unipay.entity.*;
import unipay.exception.OrderNotFoundException;
import unipay.mapper.OrderMapper;
import unipay.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for creating, updating and retrieving orders.
 * It handles balance checks, persistence and mapping to DTOs.
 */
@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepo;
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final OrderMapper mapper;

    /**
     * Constructs the OrderService with required dependencies.
     *
     * @param orderRepo         repository for Order entities
     * @param restaurantService service to look up restaurants
     * @param userService       service to manage user balances
     * @param mapper            mapper to convert between entities and DTOs
     */
    public OrderService(OrderRepository orderRepo, RestaurantService restaurantService, UserService userService, OrderMapper mapper) {
        this.orderRepo = orderRepo;
        this.restaurantService = restaurantService;
        this.userService = userService;
        this.mapper = mapper;
    }

    /**
     * Places a new order for the given user.
     * Checks and deducts the user's balance, persists the order and its items,
     * and returns a mapped OrderResponse.
     *
     * @param userId the ID of the user placing the order
     * @param req    the order details from the client
     * @return the created order as a DTO
     */
    @Transactional
    public OrderResponse placeOrder(Long userId, OrderRequest req) {
        double amount = req.getTotalAmount();
        logger.info("Placing order: userId={}, total={}", userId, amount);

        // check and deduct user balance
        userService.checkAndDecreaseBalance(userId, amount);

        // build and save the order entity
        Restaurant rest = restaurantService.getRestaurantById(req.getRestaurantId());
        User user = userService.getUserById(userId);

        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(rest);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(amount);
        order.setOrderItems(req.getItems().stream().map(i -> {
            OrderItem it = new OrderItem();
            it.setOrder(order);
            it.setItemName(i.getItemName());
            it.setQuantity(i.getQuantity());
            it.setPrice(i.getPrice());
            return it;
        }).collect(Collectors.toList()));

        Order saved = orderRepo.save(order);
        logger.info("Order created (PENDING): id={}", saved.getId());
        return mapper.toOrderResponse(saved);
    }

    /**
     * Updates the status and optional preparation time of an existing order.
     * Credits the restaurant on completion or refunds the user on rejection.
     *
     * @param req contains orderId, new status and optional prep time
     * @return the updated order as a DTO
     * @throws OrderNotFoundException if the order does not exist
     */
    @Transactional
    public OrderResponse updateOrderStatus(OrderUpdateRequest req) {
        Order order = orderRepo.findById(req.getOrderId()).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        OrderStatus old = order.getStatus();

        order.setStatus(req.getStatus());
        order.setEstimatedPreparationTime(req.getEstimatedPreparationTime());

        double amount = order.getTotalAmount();
        String restUsername = order.getRestaurant().getName();
        Long buyerId = order.getUser().getId();

        // credit restaurant balance exactly once upon completion
        if (req.getStatus() == OrderStatus.COMPLETED && old != OrderStatus.COMPLETED) {
            userService.updateUserBalance(restUsername, amount);
            logger.info("Credited {} to restaurant '{}'", amount, restUsername);
        }

        // refund user once if order is rejected
        if (req.getStatus() == OrderStatus.REJECTED && old != OrderStatus.REJECTED) {
            userService.refundBalance(buyerId, amount);
            logger.info("Refunded {} to userId={}", amount, buyerId);
        }

        Order updated = orderRepo.save(order);
        return mapper.toOrderResponse(updated);
    }

    /**
     * Retrieves all orders for the given restaurant name.
     * Returns an empty list if the restaurant does not exist.
     *
     * @param name the name of the restaurant
     * @return list of orders as DTOs
     */
    public List<OrderResponse> getOrdersByRestaurant(String name) {
        logger.info("Fetching orders for restaurant '{}'", name);
        Restaurant rest = restaurantService.findRestaurantByName(name);
        if (rest == null) {
            return Collections.emptyList();
        }
        return orderRepo.findByRestaurantId(rest.getId()).stream().map(mapper::toOrderResponse).collect(Collectors.toList());
    }

    /**
     * Retrieves the order history for a specific user.
     * Read-only transaction for performance.
     *
     * @param userId the ID of the user
     * @return list of orders as DTOs
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(Long userId) {
        logger.info("Fetching orders for userId={}", userId);
        return orderRepo.findByUserId(userId).stream().map(mapper::toOrderResponse).collect(Collectors.toList());
    }
}

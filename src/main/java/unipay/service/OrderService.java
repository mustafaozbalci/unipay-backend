// src/main/java/unipay/service/OrderService.java
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

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepo;
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final OrderMapper mapper;

    public OrderService(OrderRepository orderRepo, RestaurantService restaurantService, UserService userService, OrderMapper mapper) {
        this.orderRepo = orderRepo;
        this.restaurantService = restaurantService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Transactional
    public OrderResponse placeOrder(Long userId, OrderRequest req) {
        logger.info("Placing order: userId={}, total={}", userId, req.getTotalAmount());
        userService.checkAndDecreaseBalance(userId, req.getTotalAmount());
        Restaurant rest = restaurantService.getRestaurantById(req.getRestaurantId());
        User user = userService.getUserById(userId);

        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(rest);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(req.getTotalAmount());
        order.setOrderItems(req.getItems().stream().map(i -> {
            OrderItem it = new OrderItem();
            it.setOrder(order);
            it.setItemName(i.getItemName());
            it.setQuantity(i.getQuantity());
            it.setPrice(i.getPrice());
            return it;
        }).collect(Collectors.toList()));
        Order saved = orderRepo.save(order);
        logger.info("Order created: id={}", saved.getId());
        return mapper.toOrderResponse(saved);
    }

    @Transactional
    public OrderResponse updateOrderStatus(OrderUpdateRequest req) {
        logger.info("Updating status: orderId={}, status={}", req.getOrderId(), req.getStatus());
        Order order = orderRepo.findById(req.getOrderId()).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        OrderStatus old = order.getStatus();
        order.setStatus(req.getStatus());
        order.setEstimatedPreparationTime(req.getEstimatedPreparationTime());
        if (req.getStatus() == OrderStatus.REJECTED && old != OrderStatus.REJECTED) {
            userService.refundBalance(order.getUser().getId(), order.getTotalAmount());
            logger.info("Refunded {} for orderId={}", order.getTotalAmount(), order.getId());
        }
        Order updated = orderRepo.save(order);
        logger.info("Order status updated: id={} {}", updated.getId(), updated.getStatus());
        return mapper.toOrderResponse(updated);
    }

    public List<OrderResponse> getOrdersByRestaurant(String name) {
        logger.info("Fetching orders for restaurant '{}'", name);
        Restaurant rest = restaurantService.findRestaurantByName(name);
        if (rest == null) return Collections.emptyList();
        return orderRepo.findByRestaurantId(rest.getId()).stream().map(mapper::toOrderResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(Long userId) {
        logger.info("Fetching orders for userId={}", userId);
        return orderRepo.findByUserId(userId).stream().map(mapper::toOrderResponse).collect(Collectors.toList());
    }
}

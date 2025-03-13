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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, RestaurantService restaurantService,
                        UserService userService, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.restaurantService = restaurantService;
        this.userService = userService;
        this.orderMapper = orderMapper;
    }

    // Sipariş Verme İşlemi (Yazma İşlemi)
    @Transactional
    public OrderResponse placeOrder(Long userId, OrderRequest orderRequest) {
        logger.info("placeOrder() - Start, userId: {}, orderRequest: {}", userId, orderRequest);

        // 1) Bakiye kontrol - Düş
        double totalOrderPrice = orderRequest.getTotalAmount();
        logger.info("Checking and decreasing balance. userId: {}, totalOrderPrice: {}", userId, totalOrderPrice);
        userService.checkAndDecreaseBalance(userId, totalOrderPrice);

        // 2) Restaurant bul
        logger.info("Fetching restaurant with ID: {}", orderRequest.getRestaurantId());
        Restaurant restaurant = restaurantService.getRestaurantById(orderRequest.getRestaurantId());

        // 3) User entity
        logger.info("Fetching user with ID: {}", userId);
        User user = userService.getUserById(userId);

        // 4) Order entity oluştur
        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(totalOrderPrice);

        // 5) Sipariş öğeleri
        logger.info("Mapping OrderItems from orderRequest");
        List<OrderItem> orderItems = orderRequest.getItems().stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItemName(item.getItemName());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());
            return orderItem;
        }).collect(Collectors.toList());
        order.setOrderItems(orderItems);

        // 6) Kaydet
        Order savedOrder = orderRepository.save(order);
        logger.info("Order placed successfully with ID: {}", savedOrder.getId());

        // 7) MapStruct
        OrderResponse response = orderMapper.toOrderResponse(savedOrder);
        logger.info("placeOrder() - End, orderResponse: {}", response);
        return response;
    }


    // Sipariş Durumu Güncelleme (Yazma İşlemi)
    @Transactional
    public OrderResponse updateOrderStatus(OrderUpdateRequest orderUpdateRequest) {
        logger.info("updateOrderStatus() - Start, orderUpdateRequest: {}", orderUpdateRequest);
        logger.info("Updating order status for order ID: {}", orderUpdateRequest.getOrderId());

        Order order = orderRepository.findById(orderUpdateRequest.getOrderId()).orElseThrow(() -> {
            logger.error("Order not found with ID: {}", orderUpdateRequest.getOrderId());
            return new OrderNotFoundException("Order not found");
        });

        OrderStatus oldStatus = order.getStatus();
        order.setStatus(orderUpdateRequest.getStatus());
        order.setEstimatedPreparationTime(orderUpdateRequest.getEstimatedPreparationTime());

        Order updatedOrder = orderRepository.save(order);
        logger.info("Order status updated successfully for order ID: {}. Old status: {}, New status: {}",
                updatedOrder.getId(), oldStatus, updatedOrder.getStatus());

        OrderResponse response = orderMapper.toOrderResponse(updatedOrder);
        logger.info("updateOrderStatus() - End, updatedOrderResponse: {}", response);
        return response;
    }

    // Restoranın Siparişlerini Getirme (Okuma İşlemi)
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByRestaurant(Long restaurantId) {
        logger.info("getOrdersByRestaurant() - Start, restaurantId: {}", restaurantId);
        logger.info("Fetching orders for restaurant ID: {}", restaurantId);

        List<OrderResponse> orders = orderRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());

        logger.info("Fetched {} orders for restaurant ID: {}", orders.size(), restaurantId);
        logger.info("getOrdersByRestaurant() - End");
        return orders;
    }

    // Kullanıcının Sipariş Geçmişini Getirme (Okuma İşlemi)
    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(Long userId) {
        logger.info("getUserOrders() - Start, userId: {}", userId);
        logger.info("Fetching orders for user ID: {}", userId);

        List<OrderResponse> orders = orderRepository.findByUserId(userId)
                .stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());

        logger.info("Fetched {} orders for user ID: {}", orders.size(), userId);
        logger.info("getUserOrders() - End");
        return orders;
    }
}

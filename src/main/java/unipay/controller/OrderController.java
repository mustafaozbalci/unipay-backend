package unipay.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unipay.dto.OrderRequest;
import unipay.dto.OrderResponse;
import unipay.dto.OrderUpdateRequest;
import unipay.dto.RestaurantOrdersRequest;
import unipay.service.OrderService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Sipariş Verme
     */
    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestHeader("userId") Long userId, @Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.placeOrder(userId, orderRequest);
        ensureItemsNonNull(orderResponse);
        return ResponseEntity.ok(orderResponse);
    }

    /**
     * Restoranın Siparişlerini Görüntüleme
     */
    @PostMapping("/restaurant/orders")
    public ResponseEntity<List<OrderResponse>> getOrdersByRestaurant(@Valid @RequestBody RestaurantOrdersRequest restaurantOrdersRequest) {
        List<OrderResponse> orders = orderService.getOrdersByRestaurant(restaurantOrdersRequest.getRestaurantId());
        orders.forEach(this::ensureItemsNonNull);
        return ResponseEntity.ok(orders);
    }

    /**
     * Kullanıcının Sipariş Geçmişini Getirme
     */
    @PostMapping("/user/orders")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@RequestBody Long userId) {
        List<OrderResponse> orders = orderService.getUserOrders(userId);
        orders.forEach(this::ensureItemsNonNull);
        return ResponseEntity.ok(orders);
    }

    /**
     * Sipariş Durumu Güncelleme
     */
    @PostMapping("/update-status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@Valid @RequestBody OrderUpdateRequest orderUpdateRequest) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(orderUpdateRequest);
        ensureItemsNonNull(updatedOrder);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Yardımcı metot: orderResponse.getItems() null ise boş liste ata
     */
    private void ensureItemsNonNull(OrderResponse orderResponse) {
        if (orderResponse.getItems() == null) {
            orderResponse.setItems(new ArrayList<>());
        }
    }
}


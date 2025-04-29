// src/main/java/unipay/controller/OrderController.java
package unipay.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import unipay.dto.OrderRequest;
import unipay.dto.OrderResponse;
import unipay.dto.OrderUpdateRequest;
import unipay.dto.RestaurantOrdersRequest;
import unipay.service.OrderService;
import unipay.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(@AuthenticationPrincipal UserDetails auth, @Valid @RequestBody OrderRequest orderRequest) {

        Long userId = userService.getUserByUsername(auth.getUsername()).getId();
        OrderResponse resp = orderService.placeOrder(userId, orderRequest);
        ensureItemsNonNull(resp);
        return ResponseEntity.ok(resp);
    }

    @PreAuthorize("hasRole('RESTAURANT')")
    @PostMapping("/restaurant/orders")
    public ResponseEntity<List<OrderResponse>> getOrdersByRestaurant(@Valid @RequestBody RestaurantOrdersRequest restaurantOrdersRequest) {

        List<OrderResponse> orders = orderService.getOrdersByRestaurant(restaurantOrdersRequest.getName());
        orders.forEach(this::ensureItemsNonNull);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/orders")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@AuthenticationPrincipal UserDetails auth) {

        Long userId = userService.getUserByUsername(auth.getUsername()).getId();
        List<OrderResponse> orders = orderService.getUserOrders(userId);
        orders.forEach(this::ensureItemsNonNull);
        return ResponseEntity.ok(orders);
    }
    @PreAuthorize("hasRole('RESTAURANT')")
    @PostMapping("/update-status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@Valid @RequestBody OrderUpdateRequest orderUpdateRequest) {

        OrderResponse updated = orderService.updateOrderStatus(orderUpdateRequest);
        ensureItemsNonNull(updated);
        return ResponseEntity.ok(updated);
    }

    private void ensureItemsNonNull(OrderResponse orderResponse) {
        if (orderResponse.getItems() == null) {
            orderResponse.setItems(new ArrayList<>());
        }
    }
}

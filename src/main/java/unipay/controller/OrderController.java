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

/**
 * REST controller for managing orders.
 * <p>
 * Provides endpoints for:
 * - Placing a new order
 * - Retrieving orders for a specific restaurant (secured)
 * - Retrieving orders for the authenticated user
 * - Updating the status of an existing order (secured)
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    /**
     * Constructs a new OrderController with required services.
     *
     * @param orderService service handling order business logic
     * @param userService  service for retrieving user information
     */
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    /**
     * Places a new order on behalf of the authenticated user.
     *
     * @param auth         injected authentication principal
     * @param orderRequest validated order details from client
     * @return the created OrderResponse wrapped in HTTP 200 OK
     */
    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(@AuthenticationPrincipal UserDetails auth, @Valid @RequestBody OrderRequest orderRequest) {
        Long userId = userService.getUserByUsername(auth.getUsername()).getId();
        OrderResponse resp = orderService.placeOrder(userId, orderRequest);
        ensureItemsNonNull(resp);
        return ResponseEntity.ok(resp);
    }

    /**
     * Retrieves all orders for the specified restaurant.
     * <p>
     * Access restricted to users with the RESTAURANT role.
     *
     * @param restaurantOrdersRequest request containing the restaurant name
     * @return list of OrderResponse objects wrapped in HTTP 200 OK
     */
    @PreAuthorize("hasRole('RESTAURANT')")
    @PostMapping("/restaurant/orders")
    public ResponseEntity<List<OrderResponse>> getOrdersByRestaurant(@Valid @RequestBody RestaurantOrdersRequest restaurantOrdersRequest) {
        List<OrderResponse> orders = orderService.getOrdersByRestaurant(restaurantOrdersRequest.getName());
        orders.forEach(this::ensureItemsNonNull);
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves the order history for the authenticated user.
     *
     * @param auth injected authentication principal
     * @return list of OrderResponse objects wrapped in HTTP 200 OK
     */
    @GetMapping("/user/orders")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@AuthenticationPrincipal UserDetails auth) {
        Long userId = userService.getUserByUsername(auth.getUsername()).getId();
        List<OrderResponse> orders = orderService.getUserOrders(userId);
        orders.forEach(this::ensureItemsNonNull);
        return ResponseEntity.ok(orders);
    }

    /**
     * Updates the status of an existing order.
     * <p>
     * Access restricted to users with the RESTAURANT role.
     *
     * @param orderUpdateRequest validated payload containing order ID and new status
     * @return the updated OrderResponse wrapped in HTTP 200 OK
     */
    @PreAuthorize("hasRole('RESTAURANT')")
    @PostMapping("/update-status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@Valid @RequestBody OrderUpdateRequest orderUpdateRequest) {
        OrderResponse updated = orderService.updateOrderStatus(orderUpdateRequest);
        ensureItemsNonNull(updated);
        return ResponseEntity.ok(updated);
    }

    /**
     * Ensures that the items list in the response is never null,
     * preventing potential serialization issues on the client side.
     *
     * @param orderResponse the response object to sanitize
     */
    private void ensureItemsNonNull(OrderResponse orderResponse) {
        if (orderResponse.getItems() == null) {
            orderResponse.setItems(new ArrayList<>());
        }
    }
}

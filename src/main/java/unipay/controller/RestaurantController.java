package unipay.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import unipay.dto.RestaurantCreateRequest;
import unipay.dto.RestaurantNameUpdateRequest;
import unipay.entity.Restaurant;
import unipay.service.RestaurantService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing restaurants.
 * Provides endpoints to list, add, update, and delete restaurants.
 */
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    /**
     * Constructs the controller with the required RestaurantService.
     *
     * @param restaurantService service handling restaurant operations
     */
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /**
     * Returns the names of all restaurants.
     * Accessible by anyone (no authentication required).
     *
     * @return HTTP 200 with a list of restaurant names
     */
    @PreAuthorize("permitAll()")
    @PostMapping("/list")
    public ResponseEntity<List<String>> getAllRestaurants() {
        List<String> restaurantNames = restaurantService.getAllRestaurants().stream().map(Restaurant::getName).collect(Collectors.toList());
        return ResponseEntity.ok(restaurantNames);
    }

    /**
     * Creates a new restaurant with the given name.
     * Restricted to users with ADMIN role.
     *
     * @param request contains the name of the restaurant to create
     * @return HTTP 200 with a success message including the new restaurant ID
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<String> addRestaurant(@RequestBody RestaurantCreateRequest request) {
        Restaurant restaurant = restaurantService.addRestaurant(request.getName());
        return ResponseEntity.ok("Restaurant added successfully with ID: " + restaurant.getId());
    }

    /**
     * Updates the name of an existing restaurant.
     * Restricted to users with ADMIN role.
     *
     * @param id      the ID of the restaurant to update
     * @param request contains the new restaurant name
     * @return HTTP 200 with a success message including the updated name
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/{id}")
    public ResponseEntity<String> updateRestaurant(@PathVariable Long id, @RequestBody RestaurantNameUpdateRequest request) {
        Restaurant updatedRestaurant = restaurantService.updateRestaurant(id, request.getName());
        return ResponseEntity.ok("Restaurant updated successfully: " + updatedRestaurant.getName());
    }

    /**
     * Deletes the restaurant with the given ID.
     * Restricted to users with ADMIN role.
     *
     * @param id the ID of the restaurant to delete
     * @return HTTP 200 with a success message
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok("Restaurant deleted successfully");
    }
}

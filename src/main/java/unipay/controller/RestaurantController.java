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

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    // Tüm restoranları listeleme
    @PreAuthorize("permitAll()")
    @PostMapping("/list")
    public ResponseEntity<List<String>> getAllRestaurants() {
        List<String> restaurantNames = restaurantService.getAllRestaurants().stream().map(Restaurant::getName).collect(Collectors.toList());
        return ResponseEntity.ok(restaurantNames);
    }

    // Yeni restoran ekleme
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<String> addRestaurant(@RequestBody RestaurantCreateRequest request) {
        Restaurant restaurant = restaurantService.addRestaurant(request.getName());
        return ResponseEntity.ok("Restaurant added successfully with ID: " + restaurant.getId());
    }

    // Restoran adı güncelleme
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/{id}")
    public ResponseEntity<String> updateRestaurant(@PathVariable Long id, @RequestBody RestaurantNameUpdateRequest request) {
        Restaurant updatedRestaurant = restaurantService.updateRestaurant(id, request.getName());
        return ResponseEntity.ok("Restaurant updated successfully: " + updatedRestaurant.getName());
    }

    // Restoran silme
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok("Restaurant deleted successfully");
    }
}

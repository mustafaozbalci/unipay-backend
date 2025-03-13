//package Bilgi.TDM.Service;
//
//import Bilgi.TDM.Entity.Restaurant;
//import Bilgi.TDM.Exception.RestaurantNotFoundException;
//import Bilgi.TDM.Repository.RestaurantRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class RestaurantServiceTest {
//
//    @Mock
//    private RestaurantRepository restaurantRepository;
//
//    @InjectMocks
//    private RestaurantService restaurantService;
//
//    @Test
//    public void testGetAllRestaurants() {
//        // Arrange
//        Restaurant r1 = new Restaurant();
//        r1.setId(1L);
//        r1.setName("Restaurant 1");
//
//        Restaurant r2 = new Restaurant();
//        r2.setId(2L);
//        r2.setName("Restaurant 2");
//
//        when(restaurantRepository.findAll()).thenReturn(Arrays.asList(r1, r2));
//
//        // Act
//        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
//
//        // Assert
//        assertNotNull(restaurants);
//        assertEquals(2, restaurants.size());
//    }
//
//    @Test
//    public void testGetRestaurantById_Success() {
//        // Arrange
//        Restaurant r = new Restaurant();
//        r.setId(1L);
//        r.setName("Test Restaurant");
//
//        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));
//
//        // Act
//        Restaurant restaurant = restaurantService.getRestaurantById(1L);
//
//        // Assert
//        assertNotNull(restaurant);
//        assertEquals("Test Restaurant", restaurant.getName());
//    }
//
//    @Test
//    public void testGetRestaurantById_NotFound() {
//        // Arrange
//        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        Exception exception = assertThrows(RestaurantNotFoundException.class, () -> {
//            restaurantService.getRestaurantById(1L);
//        });
//        assertTrue(exception.getMessage().contains("Restaurant not found"));
//    }
//}

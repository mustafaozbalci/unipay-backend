package unipay.service;

import unipay.entity.Restaurant;
import unipay.exception.RestaurantNotFoundException;
import unipay.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestaurantService {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantService.class);
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    // Tüm restoranları listeleme (Okuma İşlemi)
    @Transactional(readOnly = true)
    public List<Restaurant> getAllRestaurants() {
        logger.info("getAllRestaurants() - Start");
        logger.info("Fetching all restaurants");
        List<Restaurant> restaurants = restaurantRepository.findAll();
        logger.info("Fetched {} restaurants", restaurants.size());
        logger.info("getAllRestaurants() - End");
        return restaurants;
    }

    // Restoran ID ile getirme (Okuma İşlemi)
    @Transactional(readOnly = true)
    public Restaurant getRestaurantById(Long id) {
        logger.info("getRestaurantById() - Start, restaurantId: {}", id);
        logger.info("Fetching restaurant with ID: {}", id);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Restaurant not found with ID: {}", id);
                    return new RestaurantNotFoundException("Restaurant not found with ID: " + id);
                });
        logger.info("Restaurant fetched successfully: {}", restaurant.getName());
        logger.info("getRestaurantById() - End");
        return restaurant;
    }

    // Yeni restoran ekleme (Yazma İşlemi)
    @Transactional
    public Restaurant addRestaurant(String name) {
        logger.info("addRestaurant() - Start, name: {}", name);
        logger.info("Adding new restaurant with name: {}", name);
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        logger.info("Restaurant added successfully with ID: {}", savedRestaurant.getId());
        logger.info("addRestaurant() - End");
        return savedRestaurant;
    }

    // Restoran adı güncelleme (Yazma İşlemi)
    @Transactional
    public Restaurant updateRestaurant(Long id, String name) {
        logger.info("updateRestaurant() - Start, id: {}, name: {}", id, name);
        logger.info("Updating restaurant with ID: {}", id);

        Restaurant existingRestaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Restaurant not found with ID: {}", id);
                    return new RestaurantNotFoundException("Restaurant not found with ID: " + id);
                });
        String oldName = existingRestaurant.getName();
        existingRestaurant.setName(name);
        Restaurant updatedRestaurant = restaurantRepository.save(existingRestaurant);
        logger.info("Restaurant updated successfully. Old name: {}, New name: {}", oldName, updatedRestaurant.getName());

        logger.info("updateRestaurant() - End");
        return updatedRestaurant;
    }

    // Restoran silme (Yazma İşlemi)
    @Transactional
    public void deleteRestaurant(Long id) {
        logger.info("deleteRestaurant() - Start, id: {}", id);
        logger.info("Deleting restaurant with ID: {}", id);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Restaurant not found with ID: {}", id);
                    return new RestaurantNotFoundException("Restaurant not found with ID: " + id);
                });
        restaurantRepository.delete(restaurant);
        logger.info("Restaurant with ID: {} deleted successfully", id);
        logger.info("deleteRestaurant() - End");
    }
    public Restaurant findRestaurantByName(String name) {
        // Örneğin, findByName metodu repository'de tanımlı olsun
        return restaurantRepository.findByName(name);
    }
}

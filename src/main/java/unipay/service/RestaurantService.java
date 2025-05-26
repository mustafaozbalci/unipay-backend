package unipay.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unipay.entity.Restaurant;
import unipay.exception.RestaurantNotFoundException;
import unipay.repository.RestaurantRepository;

import java.util.List;

/**
 * Service for managing Restaurant entities.
 * Handles retrieval, creation, update, and deletion operations.
 */
@Service
public class RestaurantService {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantService.class);
    private final RestaurantRepository repository;

    /**
     * Constructor injection of the RestaurantRepository.
     *
     * @param repository repository for accessing Restaurant data
     */
    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all restaurants.
     *
     * @return list of all Restaurant entities
     */
    @Transactional(readOnly = true)
    public List<Restaurant> getAllRestaurants() {
        logger.info("Listing all restaurants");
        return repository.findAll();
    }

    /**
     * Finds a restaurant by its ID.
     *
     * @param id the ID of the restaurant
     * @return the Restaurant entity
     * @throws RestaurantNotFoundException if no restaurant is found
     */
    @Transactional(readOnly = true)
    public Restaurant getRestaurantById(Long id) {
        logger.info("Getting restaurant id={}", id);
        return repository.findById(id).orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found: " + id));
    }

    /**
     * Adds a new restaurant with the given name.
     *
     * @param name the name of the new restaurant
     * @return the saved Restaurant entity
     */
    @Transactional
    public Restaurant addRestaurant(String name) {
        logger.info("Adding restaurant '{}'", name);
        Restaurant saved = repository.save(new Restaurant(null, name));
        logger.info("Added restaurant id={}", saved.getId());
        return saved;
    }

    /**
     * Updates the name of an existing restaurant.
     *
     * @param id   the ID of the restaurant to update
     * @param name the new name to set
     * @return the updated Restaurant entity
     */
    @Transactional
    public Restaurant updateRestaurant(Long id, String name) {
        logger.info("Updating restaurant id={} to '{}'", id, name);
        Restaurant existing = getRestaurantById(id);
        String oldName = existing.getName();
        existing.setName(name);
        Restaurant updated = repository.save(existing);
        logger.info("Restaurant id={} renamed '{}'→'{}'", id, oldName, updated.getName());
        return updated;
    }

    /**
     * Deletes a restaurant by its ID.
     *
     * @param id the ID of the restaurant to delete
     */
    @Transactional
    public void deleteRestaurant(Long id) {
        logger.info("Deleting restaurant id={}", id);
        getRestaurantById(id);
        repository.deleteById(id);
        logger.info("Deleted restaurant id={}", id);
    }

    /**
     * Finds a restaurant by its exact name.
     *
     * @param name the name of the restaurant
     * @return the matching Restaurant entity, or null if not found
     */
    public Restaurant findRestaurantByName(String name) {
        return repository.findByName(name);
    }
}

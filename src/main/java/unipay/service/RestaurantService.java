// src/main/java/unipay/service/RestaurantService.java
package unipay.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unipay.entity.Restaurant;
import unipay.exception.RestaurantNotFoundException;
import unipay.repository.RestaurantRepository;

import java.util.List;

@Service
public class RestaurantService {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantService.class);
    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Restaurant> getAllRestaurants() {
        logger.info("Listing all restaurants");
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Restaurant getRestaurantById(Long id) {
        logger.info("Getting restaurant id={}", id);
        return repository.findById(id).orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found: " + id));
    }

    @Transactional
    public Restaurant addRestaurant(String name) {
        logger.info("Adding restaurant '{}'", name);
        Restaurant saved = repository.save(new Restaurant(null, name));
        logger.info("Added restaurant id={}", saved.getId());
        return saved;
    }

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

    @Transactional
    public void deleteRestaurant(Long id) {
        logger.info("Deleting restaurant id={}", id);
        getRestaurantById(id);
        repository.deleteById(id);
        logger.info("Deleted restaurant id={}", id);
    }

    public Restaurant findRestaurantByName(String name) {
        return repository.findByName(name);
    }
}

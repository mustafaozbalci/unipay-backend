package unipay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unipay.entity.Restaurant;

import java.util.List;

/**
 * Repository interface for Restaurant entities.
 * Extends JpaRepository to provide standard CRUD operations and custom queries.
 */
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    /**
     * Finds all restaurants whose names contain the given substring, ignoring case.
     *
     * @param name substring to search within restaurant names
     * @return list of matching Restaurant entities
     */
    List<Restaurant> findByNameContainingIgnoreCase(String name);

    /**
     * Retrieves a single restaurant by its exact name.
     *
     * @param name the exact name of the restaurant
     * @return the matching Restaurant entity, or null if none found
     */
    Restaurant findByName(String name);

    /**
     * Checks whether a restaurant with the given name exists.
     *
     * @param name the name to check for existence
     * @return true if a restaurant with that name exists, false otherwise
     */
    boolean existsByName(String name);
}

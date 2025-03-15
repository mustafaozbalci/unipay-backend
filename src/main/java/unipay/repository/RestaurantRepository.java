package unipay.repository;

import unipay.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByNameContainingIgnoreCase(String name); // Restoran ismine göre arama
    Restaurant findByName (String name);

    boolean existsByName(String espressolab);
}

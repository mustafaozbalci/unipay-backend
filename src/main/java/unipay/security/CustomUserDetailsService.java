package unipay.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import unipay.entity.User;
import unipay.repository.RestaurantRepository;
import unipay.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Loads application users into Spring Security, assigning appropriate roles.
 * All users receive ROLE_USER. The otopark service user also receives ROLE_ADMIN.
 * Any user matching a restaurant name in the database receives ROLE_RESTAURANT.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UserRepository userRepo;
    private final RestaurantRepository restaurantRepo;

    /**
     * Constructs the service with required repositories.
     *
     * @param userRepo       repository for User entities
     * @param restaurantRepo repository for Restaurant entities
     */
    public CustomUserDetailsService(UserRepository userRepo, RestaurantRepository restaurantRepo) {
        this.userRepo = userRepo;
        this.restaurantRepo = restaurantRepo;
    }

    /**
     * Retrieves a user by username and builds a Spring Security UserDetails object.
     * Assigns roles based on user type:
     * - ROLE_USER: every authenticated user
     * - ROLE_ADMIN: if the user's email is otopark@otopark.com
     * - ROLE_RESTAURANT: if a Restaurant exists with the same name as the username
     *
     * @param username the username to look up
     * @return UserDetails containing username, password, and granted authorities
     * @throws UsernameNotFoundException if no User is found for the given username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // Every user gets the basic ROLE_USER authority
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // Grant ADMIN role to the parking service account
        if ("otopark@otopark.com".equalsIgnoreCase(user.getEmail())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            logger.info("Granted ADMIN to '{}'", username);
        }

        // Grant RESTAURANT role if the username matches a restaurant name
        if (restaurantRepo.findByName(username) != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_RESTAURANT"));
            logger.info("Granted RESTAURANT to '{}'", username);
        }

        // Build and return the Spring Security User object
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }
}

// src/main/java/unipay/security/CustomUserDetailsService.java
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

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UserRepository userRepo;
    private final RestaurantRepository restaurantRepo;

    public CustomUserDetailsService(UserRepository userRepo, RestaurantRepository restaurantRepo) {
        this.userRepo = userRepo;
        this.restaurantRepo = restaurantRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<SimpleGrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority("ROLE_USER"));
        if ("otopark@otopark.com".equalsIgnoreCase(user.getEmail())) {
            auths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            logger.info("Granted ADMIN to '{}'", username);
        }
        if (restaurantRepo.findByName(username) != null) {
            auths.add(new SimpleGrantedAuthority("ROLE_RESTAURANT"));
            logger.info("Granted RESTAURANT to '{}'", username);
        }
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), auths);
    }
}

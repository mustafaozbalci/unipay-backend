package unipay.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import unipay.entity.Restaurant;
import unipay.entity.User;
import unipay.repository.RestaurantRepository;
import unipay.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Varsayılan restoranları oluştur (eğer daha önce eklenmemişse)
        if (!restaurantRepository.existsByName("nero")) {
            Restaurant nero = new Restaurant();
            nero.setName("nero");
            restaurantRepository.save(nero);
            System.out.println("Created default restaurant: nero");
        }

        if (!restaurantRepository.existsByName("espressolab")) {
            Restaurant espressolab = new Restaurant();
            espressolab.setName("espressolab");
            restaurantRepository.save(espressolab);
            System.out.println("Created default restaurant: espressolab");
        }

        // Varsayılan restoran kullanıcıları
        if (!userRepository.existsByEmail("nero@nero.com")) {
            User neroUser = new User();
            neroUser.setUsername("nero");
            neroUser.setPassword("$2a$10$KfRFnHTS36Dfft82gm9q.uazfjkXr891b6.LcWyH/h3332YU5llNC"); // Gerçek uygulamada şifreleri encode etmeyi unutmayın.
            neroUser.setEmail("nero@nero.com");
            neroUser.setBalance(0.0);
            userRepository.save(neroUser);
            System.out.println("Created default user for nero");
        }

        if (!userRepository.existsByEmail("espressolab@espressolab.com")) {
            User espressoUser = new User();
            espressoUser.setUsername("espressolab");
            espressoUser.setPassword("$2a$10$KfRFnHTS36Dfft82gm9q.uazfjkXr891b6.LcWyH/h3332YU5llNC"); // Gerçek uygulamada şifreleri encode etmeyi unutmayın.
            espressoUser.setEmail("espressolab@espressolab.com");
            espressoUser.setBalance(0.0);
            userRepository.save(espressoUser);
            System.out.println("Created default user for espressolab");
        }

        // Üçüncü default user: mustafa
        if (!userRepository.existsByEmail("mustafa@unipay.com") && !userRepository.existsByUsername("mustafa")) {
            User defaultUser = new User();
            defaultUser.setUsername("mustafa");
            defaultUser.setPassword("$2a$10$KfRFnHTS36Dfft82gm9q.uazfjkXr891b6.LcWyH/h3332YU5llNC");
            defaultUser.setEmail("mustafa@unipay.com");
            defaultUser.setBalance(100000.0);
            userRepository.save(defaultUser);
            System.out.println("Created default user: mustafa");
        }

        // Yeni default user: otopark
        if (!userRepository.existsByEmail("otopark@otopark.com") && !userRepository.existsByUsername("otopark")) {
            User otoparkUser = new User();
            otoparkUser.setUsername("otopark");
            otoparkUser.setPassword("$2a$10$KfRFnHTS36Dfft82gm9q.uazfjkXr891b6.LcWyH/h3332YU5llNC");
            otoparkUser.setEmail("otopark@otopark.com");
            otoparkUser.setBalance(0.0);
            userRepository.save(otoparkUser);
            System.out.println("Created default user: otopark");
        }
    }
}

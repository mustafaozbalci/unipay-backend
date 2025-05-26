package unipay.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import unipay.entity.*;
import unipay.repository.ParkingAreaRepository;
import unipay.repository.ParkingSessionRepository;
import unipay.repository.RestaurantRepository;
import unipay.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DataLoader is a Spring component that initializes the database with
 * sample restaurants, users, parking areas, and parking sessions
 * when the application starts
 * as an example usage of the program
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final ParkingAreaRepository parkingAreaRepository;
    private final ParkingSessionRepository parkingSessionRepository;

    /**
     * Precomputed BCrypt hash for the default password "123456".
     */
    private static final String DEFAULT_HASH = "$2a$10$KfRFnHTS36Dfft82gm9q.uazfjkXr891b6.LcWyH/h3332YU5llNC";

    public DataLoader(RestaurantRepository restaurantRepository, UserRepository userRepository, ParkingAreaRepository parkingAreaRepository, ParkingSessionRepository parkingSessionRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.parkingAreaRepository = parkingAreaRepository;
        this.parkingSessionRepository = parkingSessionRepository;
    }

    /**
     * Executes on application startup. Populates:
     * 1. Restaurants and their user accounts.
     * 2. A dedicated parking user.
     * 3. Parking areas with statuses and capacities.
     * 4. Sample users (Mustafa, Tolga, Doğukan) along with example past parking sessions.
     */
    @Override
    public void run(String... args) throws Exception {
        // --- Restaurants ---
        if (!restaurantRepository.existsByName("nero")) {
            restaurantRepository.save(new Restaurant(null, "nero"));
        }
        if (!restaurantRepository.existsByName("espressolab")) {
            restaurantRepository.save(new Restaurant(null, "espressolab"));
        }

        // --- Restaurant user accounts ---
        if (!userRepository.existsByEmail("nero@nero.com")) {
            User neroUser = new User();
            neroUser.setUsername("nero");
            neroUser.setPassword(DEFAULT_HASH);
            neroUser.setEmail("nero@nero.com");
            neroUser.setBalance(0.0);
            userRepository.save(neroUser);
        }
        if (!userRepository.existsByEmail("espressolab@espressolab.com")) {
            User espressoUser = new User();
            espressoUser.setUsername("espressolab");
            espressoUser.setPassword(DEFAULT_HASH);
            espressoUser.setEmail("espressolab@espressolab.com");
            espressoUser.setBalance(0.0);
            userRepository.save(espressoUser);
        }

        // --- Parking service account ---
        if (!userRepository.existsByEmail("otopark@otopark.com") && !userRepository.existsByUsername("otopark")) {
            User parkingUser = new User();
            parkingUser.setUsername("otopark");
            parkingUser.setPassword(DEFAULT_HASH);
            parkingUser.setEmail("otopark@otopark.com");
            parkingUser.setBalance(0.0);
            userRepository.save(parkingUser);
        }

        // --- Parking areas with initial statuses ---
        if (parkingAreaRepository.count() == 0) {
            parkingAreaRepository.save(ParkingArea.builder().name("Kız Yurdu Otoparkı").status(ParkingStatus.AVAILABLE).capacity(30).topPercent("78%").leftPercent("12%").build());
            parkingAreaRepository.save(ParkingArea.builder().name("Ana Otopark").status(ParkingStatus.FULL).capacity(50).topPercent("62%").leftPercent("65%").build());
            parkingAreaRepository.save(ParkingArea.builder().name("Amfi Otoparkı").status(ParkingStatus.CLOSED).capacity(20).topPercent("20%").leftPercent("90%").build());
        }

        // --- Sample user: Mustafa and his past parking sessions ---
        if (!userRepository.existsByEmail("mustafa@unipay.com") && !userRepository.existsByUsername("mustafa")) {

            User mustafa = new User();
            mustafa.setUsername("mustafa");
            mustafa.setPassword(DEFAULT_HASH);
            mustafa.setEmail("mustafa@unipay.com");
            mustafa.setBalance(100000.00);
            mustafa.setPlate("34ABC123");
            userRepository.save(mustafa);

            List<ParkingArea> areas = parkingAreaRepository.findAll();
            if (!areas.isEmpty()) {
                ParkingArea area = areas.get(0);
                // Create three past sessions (one per day)
                for (int i = 3; i >= 1; i--) {
                    LocalDateTime enterTime = LocalDateTime.now().minusDays(i).withHour(9).withMinute(0).withSecond(0);
                    LocalDateTime exitTime = enterTime.plusHours(2);
                    BigDecimal fee = BigDecimal.valueOf(5).multiply(BigDecimal.valueOf(2));

                    ParkingSession session = new ParkingSession();
                    session.setParkingArea(area);
                    session.setUser(mustafa);
                    session.setPlate(mustafa.getPlate());
                    session.setEnterTime(enterTime);
                    session.setExitTime(exitTime);
                    session.setFee(fee);
                    parkingSessionRepository.save(session);

                    // Deduct fee from user's balance
                    mustafa.setBalance(mustafa.getBalance() - fee.doubleValue());
                }
                userRepository.save(mustafa);
            }
        }

        // --- Sample user: Tolga and his past parking sessions ---
        if (!userRepository.existsByEmail("tolga@unipay.com") && !userRepository.existsByUsername("tolga")) {

            User tolga = new User();
            tolga.setUsername("tolga");
            tolga.setPassword(DEFAULT_HASH);
            tolga.setEmail("tolga@unipay.com");
            tolga.setBalance(100000.00);
            tolga.setPlate("34TOL12");
            userRepository.save(tolga);

            List<ParkingArea> areas = parkingAreaRepository.findAll();
            if (areas.size() > 1) {
                ParkingArea area = areas.get(1);
                // Create three past sessions (one per day)
                for (int i = 3; i >= 1; i--) {
                    LocalDateTime enterTime = LocalDateTime.now().minusDays(i).withHour(10).withMinute(30).withSecond(0);
                    LocalDateTime exitTime = enterTime.plusHours(1);
                    BigDecimal fee = BigDecimal.valueOf(5).multiply(BigDecimal.valueOf(1));

                    ParkingSession session = new ParkingSession();
                    session.setParkingArea(area);
                    session.setUser(tolga);
                    session.setPlate(tolga.getPlate());
                    session.setEnterTime(enterTime);
                    session.setExitTime(exitTime);
                    session.setFee(fee);
                    parkingSessionRepository.save(session);

                    // Deduct fee from user's balance
                    tolga.setBalance(tolga.getBalance() - fee.doubleValue());
                }
                userRepository.save(tolga);
            }
        }

        // --- Sample user: Doğukan and his past parking sessions ---
        if (!userRepository.existsByEmail("dogukan@unipay.com") && !userRepository.existsByUsername("dogukan")) {

            User dogukan = new User();
            dogukan.setUsername("dogukan");
            dogukan.setPassword(DEFAULT_HASH);
            dogukan.setEmail("dogukan@unipay.com");
            dogukan.setBalance(100000.00);
            dogukan.setPlate("34DOG123");
            userRepository.save(dogukan);

            List<ParkingArea> areas = parkingAreaRepository.findAll();
            if (areas.size() > 2) {
                ParkingArea area = areas.get(2);
                // Create three past sessions (one per day)
                for (int i = 3; i >= 1; i--) {
                    LocalDateTime enterTime = LocalDateTime.now().minusDays(i).withHour(11).withMinute(15).withSecond(0);
                    LocalDateTime exitTime = enterTime.plusHours(3);
                    BigDecimal fee = BigDecimal.valueOf(5).multiply(BigDecimal.valueOf(3));

                    ParkingSession session = new ParkingSession();
                    session.setParkingArea(area);
                    session.setUser(dogukan);
                    session.setPlate(dogukan.getPlate());
                    session.setEnterTime(enterTime);
                    session.setExitTime(exitTime);
                    session.setFee(fee);
                    parkingSessionRepository.save(session);

                    // Deduct fee from user's balance
                    dogukan.setBalance(dogukan.getBalance() - fee.doubleValue());
                }
                userRepository.save(dogukan);
            }
        }
    }
}

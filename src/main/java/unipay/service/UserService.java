package unipay.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unipay.dto.*;
import unipay.entity.User;
import unipay.exception.BalanceNotEnoughException;
import unipay.exception.EmailAlreadyExistsException;
import unipay.exception.UserNotFoundException;
import unipay.exception.UsernameAlreadyExistsException;
import unipay.mapper.UserMapper;
import unipay.repository.UserRepository;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    // Constructor
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    // KULLANICI KAYDI
    @Transactional
    public UserResponse registerUser(UserRegisterRequest registerRequest) {
        logger.info("registerUser() - Start, registerRequest: {}", registerRequest);

        logger.info("Registering user with username: {}", registerRequest.getUsername());

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            logger.error("Username already exists: {}", registerRequest.getUsername());
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            logger.error("Email already exists: {}", registerRequest.getEmail());
            throw new EmailAlreadyExistsException("Email already exists");
        }

        // Yeni user oluşturma
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setStudentNumber(registerRequest.getStudentNumber());
        user.setBalance(0.0);

        User savedUser = userRepository.save(user);
        logger.info("User registered successfully with username: {}", savedUser.getUsername());

        // MapStruct ile dönüşüm
        UserResponse response = userMapper.toUserResponse(savedUser);
        logger.info("registerUser() - End, userResponse: {}", response);
        return response;
    }

    // KULLANICI GİRİŞİ
    @Transactional(readOnly = true)
    public UserResponse loginUser(UserLoginRequest loginRequest) {
        logger.info("loginUser() - Start, loginRequest: {}", loginRequest);
        logger.info("User login attempt for email: {}", loginRequest.getEmail());

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> {
            logger.error("User not found with email: {}", loginRequest.getEmail());
            return new UserNotFoundException("User not found");
        });

        // Parola eşleşmesi
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            logger.info("User login successful for email: {}", user.getEmail());
            UserResponse response = userMapper.toUserResponse(user);
            logger.info("loginUser() - End, userResponse: {}", response);
            return response;
        } else {
            logger.error("Invalid credentials for email: {}", loginRequest.getEmail());
            throw new RuntimeException("Invalid credentials");
        }
    }

    // KULLANICIYI username İLE BULMA (iç kullanım)
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        logger.info("getUserByUsername() - Start, username: {}", username);
        logger.info("Fetching user with username: {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            logger.error("User not found with username: {}", username);
            return new UserNotFoundException("User not found with username: " + username);
        });
        logger.info("getUserByUsername() - End, userId: {}", user.getId());
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        logger.info("getUserById() - Start, userId: {}", id);
        logger.info("Fetching user with id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> {
            logger.error("User not found with id: {}", id);
            return new UserNotFoundException("User not found with id: " + id);
        });
        logger.info("getUserById() - End, foundUsername: {}", user.getUsername());
        return user;
    }

    // KULLANICI DETAYLARI
    @Transactional(readOnly = true)
    public UserResponse getUserDetails(String username) {
        logger.info("getUserDetails() - Start, username: {}", username);
        logger.info("Fetching details for user with username: {}", username);

        User user = getUserByUsername(username);
        UserResponse response = userMapper.toUserResponse(user);

        logger.info("getUserDetails() - End, userResponse: {}", response);
        return response;
    }

    // YENİ METOT: Kullanıcının sadece şifresini güncelleme
    @Transactional
    public UserResponse updateUserPassword(String username, UserPasswordUpdateRequest passwordRequest) {
        logger.info("updateUserPassword() - Start, username: {}, passwordRequest: {}", username, passwordRequest);

        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            logger.error("User not found with username: {}", username);
            return new UserNotFoundException("User not found with username: " + username);
        });

        // Yalnızca şifre güncelleniyor
        user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));

        User updatedUser = userRepository.save(user);
        logger.info("User password updated successfully for username: {}", updatedUser.getUsername());

        UserResponse response = userMapper.toUserResponse(updatedUser);
        logger.info("updateUserPassword() - End, updatedUserResponse: {}", response);

        return response;
    }

    // YENİ METOT: Kullanıcının sadece bakiyesini güncelleme (set etme)
    @Transactional
    public UserResponse addBalance(String username, UserAddBalanceRequest addBalanceRequest) {
        logger.info("addBalance() - Start, username: {}, addBalanceRequest: {}", username, addBalanceRequest);

        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            logger.error("User not found with username: {}", username);
            return new UserNotFoundException("User not found with username: " + username);
        });

        double oldBalance = user.getBalance();
        double newBalance = oldBalance + addBalanceRequest.getAmountToAdd();
        user.setBalance(newBalance);

        User updatedUser = userRepository.save(user);
        logger.info("User balance updated. Old balance: {}, Amount to add: {}, New balance: {}", oldBalance, addBalanceRequest.getAmountToAdd(), newBalance);

        UserResponse response = userMapper.toUserResponse(updatedUser);
        logger.info("addBalance() - End, updatedUserResponse: {}", response);

        return response;
    }


    // PARA EKLEMEK İÇİN BASİT BİR METOT (Opsiyonel)
    // Bu metod "mevcut bakiyeye amountToAdd ekleme" mantığında çalışıyor
    // Yukarıdaki updateUserBalance ile farkı: Birebir set etmek yerine ekleme yapıyor.
    @Transactional
    public void updateUserBalance(String username, Double amountToAdd) {
        logger.info("updateUserBalance(Double) - Start, username: {}, amountToAdd: {}", username, amountToAdd);

        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            logger.error("User not found with username: {}", username);
            return new UserNotFoundException("User not found with username: " + username);
        });

        double oldBalance = user.getBalance();
        user.setBalance(oldBalance + amountToAdd);
        userRepository.save(user);

        logger.info("User balance updated. Old balance: {}, New balance: {}", oldBalance, user.getBalance());
        logger.info("updateUserBalance(Double) - End");
    }

    @Transactional
    public void checkAndDecreaseBalance(Long userId, double amount) {
        logger.info("checkAndDecreaseBalance() - Start, userId: {}, amount: {}", userId, amount);

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        logger.info("Current balance: {}. Amount to decrease: {}", user.getBalance(), amount);
        if (user.getBalance() < amount) {
            logger.error("Insufficient balance for user with ID: {}. Required: {}, Available: {}", userId, amount, user.getBalance());
            throw new BalanceNotEnoughException("Insufficient balance to place this order.");
        }
        // Bakiye yeterliyse düş
        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);
        logger.info("User balance updated. New balance: {}", user.getBalance());
        logger.info("checkAndDecreaseBalance() - End");
    }
}
